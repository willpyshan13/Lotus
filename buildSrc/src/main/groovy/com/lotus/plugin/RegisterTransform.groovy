package com.lotus.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * Created by ljq on 2019/5/9
 */
class RegisterTransform extends Transform {

    private Project mProject
    static ArrayList<ScanSetting> sRegisterList = new ArrayList<>()
    static File fileContainsInitClass

    RegisterTransform(Project project) {
        mProject = project
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs
                   , Collection<TransformInput> referencedInputs
                   , TransformOutputProvider outputProvider
                   , boolean isIncremental) throws IOException, TransformException, InterruptedException {
        Logger.i('Start scan register info in jar file.')

        long startTime = System.currentTimeMillis()
        boolean leftSlash = File.separator == '/'

        inputs.each { TransformInput input ->

            // scan all jars
            input.jarInputs.each { JarInput jarInput ->
                String destName = jarInput.name
                // rename jar files
                def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
                if (destName.endsWith(".jar")) {
                    destName = destName.substring(0, destName.length() - 4)
                }
                // input file
                File src = jarInput.file
                // output file
                File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                //scan jar file to find classes
                if (ScanUtils.shouldProcessPreDexJar(src.absolutePath)) {
                    ScanUtils.scanJar(src, dest)
                }
                FileUtils.copyFile(src, dest)

            }
            // scan class files
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                String root = directoryInput.file.absolutePath
                if (!root.endsWith(File.separator))
                    root += File.separator
                directoryInput.file.eachFileRecurse { File file ->
                    def path = file.absolutePath.replace(root, '')
                    if (!leftSlash) {
                        path = path.replaceAll("\\\\", "/")
                    }
                    if (file.isFile() && ScanUtils.shouldProcessClass(path)) {
                        ScanUtils.scanClass(file)
                    }
                }

                // copy to dest
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }

        Logger.i('Scan finish, current cost time ' + (System.currentTimeMillis() - startTime) + "ms")

        if (fileContainsInitClass) {
            sRegisterList.each { ext ->
                Logger.i('Insert register code to file ' + fileContainsInitClass.absolutePath)

                if (ext.classList.isEmpty()) {
                    Logger.e("No class implements found for interface:" + ext.interfaceName)
                } else {
                    ext.classList.each {
                        Logger.i(it)
                    }
                    RegisterCodeGenerator.insertInitCodeTo(ext)
                }
            }
        }

        Logger.i("Generate code finish, current cost time: " + (System.currentTimeMillis() - startTime) + "ms")
    }

    @Override
    String getName() {
        return ScanSetting.PLUGIN_NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }
}
