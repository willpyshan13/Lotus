package com.lotus.processor;

import com.lotus.annotation.ILotusImplProvider;
import com.lotus.annotation.ILotusProxyProvider;
import com.lotus.annotation.LotusImpl;
import com.lotus.annotation.LotusProxy;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by ljq on 2019/5/8
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({Consts.ANNOTATION_IMPL, Consts.ANNOTATION_PROXY})
@AutoService(Processor.class)
public class LotusProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Filer mFiler;
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        Map<String, String> options = processingEnvironment.getOptions();
        if (!options.isEmpty()) {
            moduleName = options.get("moduleName");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> lotusImpls = roundEnvironment.getElementsAnnotatedWith(LotusImpl.class);
        Set<? extends Element> lotusProxys = roundEnvironment.getElementsAnnotatedWith(LotusProxy.class);
        if (lotusImpls != null && lotusImpls.size() > 0) {
            createLotusImpl(lotusImpls, roundEnvironment);
        }
        if (lotusProxys != null && lotusProxys.size() > 0) {
            createLotusProxy(lotusProxys, roundEnvironment);
        }
        return true;
    }

    /**
     * 创建LotusProxyProvider类，实现ILotusProxyProvider接口
     * 创建HashMap，存放所有LotusProxy注解的类的映射
     * 包名增加moduleName用于区分不同mudule，避免生成多个相同包名的LotusProxyProvider产生冲突
     */
    private void createLotusProxy(Set<? extends Element> lotusPrixy, RoundEnvironment roundEnvironment) {
        String pck = Consts.LOTUS_PACKAGE + "." + moduleName;
        TypeSpec.Builder builder = TypeSpec.classBuilder("LotusProxyProvider")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ILotusProxyProvider.class);

        //Map<String, Class<?>>
        WildcardTypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
        ParameterizedTypeName classOfAny = ParameterizedTypeName.get(ClassName.get(Class.class), wildcard);
        ClassName string = ClassName.get(String.class);
        TypeName map = ParameterizedTypeName.get(ClassName.get(HashMap.class), string, classOfAny);

        FieldSpec.Builder mapBuild = FieldSpec.builder(map, "map", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new HashMap<>()");

        CodeBlock.Builder blockBuilder = CodeBlock.builder();

        for (Element element : lotusPrixy) {
            LotusProxy annotation = element.getAnnotation(LotusProxy.class);
            TypeElement enclosingElement = (TypeElement) element;
            ClassName className = ClassName.get(enclosingElement);
            blockBuilder.addStatement("map.put(\"$L\", $T.class)", annotation.value(), className);
        }

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("getMap")
                .returns(Map.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addCode("return map;\n");

        builder.addField(mapBuild.build());
        builder.addStaticBlock(blockBuilder.build());
        builder.addMethod(methodBuild.build());
        JavaFile file = JavaFile.builder(pck, builder.build()).build();
        try {
            file.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建LotusImplProvider类，实现ILotusImplProvider接口
     * 创建HashMap，存放所有LotusImpl注解的类的映射
     * 包名增加moduleName用于区分不同mudule，避免生成多个相同包名的LotusProxyProvider产生冲突
     */
    private void createLotusImpl(Set<? extends Element> lotusImpls, RoundEnvironment roundEnvironment) {
        String pck = Consts.LOTUS_PACKAGE + "." + moduleName;
        TypeSpec.Builder builder = TypeSpec.classBuilder("LotusImplProvider")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ILotusImplProvider.class);

        //Map<String, Class<?>>
        WildcardTypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
        ParameterizedTypeName classOfAny = ParameterizedTypeName.get(ClassName.get(Class.class), wildcard);
        ClassName string = ClassName.get(String.class);
        TypeName map = ParameterizedTypeName.get(ClassName.get(HashMap.class), classOfAny, string);

        FieldSpec.Builder mapBuild = FieldSpec.builder(map, "map", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new HashMap<>()");

        CodeBlock.Builder blockBuilder = CodeBlock.builder();

        for (Element element : lotusImpls) {
            LotusImpl annotation = element.getAnnotation(LotusImpl.class);
            TypeElement enclosingElement = (TypeElement) element;
            ClassName className = ClassName.get(enclosingElement);
            blockBuilder.addStatement("map.put($T.class, \"$L\")", className, annotation.value());
        }

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("getMap")
                .returns(Map.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addCode("return map;\n");

        builder.addField(mapBuild.build());
        builder.addStaticBlock(blockBuilder.build());
        builder.addMethod(methodBuild.build());
        JavaFile file = JavaFile.builder(pck, builder.build()).build();
        try {
            file.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
