/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.omscs_starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class StarterApplication extends Application {

  @Override
    public void onCreate() {
      super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("86f66276e2f31dcab521ce051d29223c848c2af6")
            .clientKey("66f22b0e6a2fcaf968ebda254e4b4813dc850606")
            .server("http://ec2-13-59-203-173.us-east-2.compute.amazonaws.com:80/parse/")
            .build()
    );



    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL,true);
  }
}
