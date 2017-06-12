package swk.application.helper;

import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by Martin Endres on 08.12.2016.
 * Class to detect if Device is rooted
 */

public class RootDetection {

        public boolean isRootedDevice() {
            return checkRootMethodTags() || checkRootMethodPathExistence() || checkRootMethodSuperUser() || checkRootMethodRootUtils();
        }

        private boolean checkRootMethodTags() {
            //Check existence of Build-Tag "test-keys"
            String buildTags = android.os.Build.TAGS;
            if (buildTags!=null && buildTags.contains("test-keys")){
                return true;
            }else{
                return false;
            }
        }

        private boolean checkRootMethodPathExistence() {
            //Check existence of several common Root-Files
            String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                    "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su", "/su/xbin"};
            for (String path : paths) {
                //If one of the given Paths exists the Device is Rooted
                if (new File(path).exists()){
                    return true;
                }
            }
            return false;
        }

        private boolean checkRootMethodSuperUser() {
            Process shellCommand= null;
            try {
                //Try to access SuperUser by Runtime (comparable with Unix-Acess)
                shellCommand = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
                BufferedReader in = new BufferedReader(new InputStreamReader(shellCommand.getInputStream()));
                //There will be only an BufferedReader if the SuperUser is accessible
                if (in.readLine() != null){
                    return true;
                }
                return false;
            } catch (Throwable t) {
                //Ignore Exception cause is desired at this point
                return false;
            } finally {
                //For the case that the Device is rooted close the Instance of shellCommand and required to avoid Code Zombies (https://code.google.com/p/android/issues/detail?id=55017)
                if (shellCommand != null){
                    shellCommand.destroy();
                }
            }
        }

        private boolean checkRootMethodRootUtils(){
            //Check with external Library RootTools which provides SuperUser-Detection (https://github.com/Stericson/RootTools)
            if (RootTools.isAccessGiven()){
                return true;
            }else{
                return false;
            }
        }
}
