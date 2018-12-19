package ru.lopav.kzn.fb.deepLink;

import com.airbnb.deeplinkdispatch.DeepLinkSpec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Prefix all app deep link URIs with "mfbcas://screen"
@DeepLinkSpec(prefix = { "mfbcas://screen" })
@Retention(RetentionPolicy.CLASS)
public @interface  AppLinks {
    String[] value();
}
