package com.minerva.helpforward;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IsCompanyViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isCompany = new MutableLiveData<>();
    public IsCompanyViewModel() {
        isCompany.setValue(null);
    }

    public LiveData<Boolean> getIsCompany(){
        return isCompany;
    }

    public void setIsCompany(boolean isCompany){
        this.isCompany.setValue(isCompany);
    }
}
