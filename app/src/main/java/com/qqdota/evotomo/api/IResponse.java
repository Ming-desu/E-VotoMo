package com.qqdota.evotomo.api;

import com.qqdota.evotomo.models.SessionResponse;

public interface IResponse {
    interface OnSuccessListener {
        void onSuccess();
    }

    interface OnErrorListener {
        void onError();
    }
}
