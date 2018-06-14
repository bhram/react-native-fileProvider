package com.bhram.fileprovider;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;

public class RNReactNativeFileProviderModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNReactNativeFileProviderModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @ReactMethod
  public void openFile(String filepath,String mime,String authority,Promise promise){
   try {
     filepath = filepath.replace("file://", "");
     File file = new File(filepath);
     if (!file.exists()) throw new Exception("File does not exist");
     Uri path  = FileProvider.getUriForFile(this.getReactApplicationContext(),
             authority,file);
     Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
     pdfIntent.setDataAndType(path, mime);
     pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     reactContext.startActivity(pdfIntent);
     promise.resolve(path.toString());

   } catch (Exception ex) {
      ex.printStackTrace();
      reject(promise, filepath, ex);
    }
  }
  private void reject(Promise promise, String filepath, Exception ex) {
    promise.reject(null, ex.getMessage());
  }

  @Override
  public String getName() {
    return "RNReactNativeFileProvider";
  }
}
