package me.ztiany.asm;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AgentParams {

    public static void main(String[] args) {
        AgentParams agentParams = new AgentParams();
        agentParams.setAdapterArguments(new ArrayList<>());
        agentParams.setApplyingPackage(new ArrayList<>());
        agentParams.setAdapterName("RemoveMethodAdapter");
        System.out.println(new Gson().toJson(agentParams));
    }

    @SerializedName("applyingPackage")
    private List<String> mApplyingPackage = new ArrayList<>();

    @SerializedName("adapterArguments")
    private List<String> mAdapterArguments = new ArrayList<>();

    @SerializedName("adapterName")
    private String mAdapterName = "";

    public List<String> getApplyingPackage() {
        return mApplyingPackage;
    }

    public void setApplyingPackage(List<String> applyingPackage) {
        mApplyingPackage = applyingPackage;
    }

    public List<String> getAdapterArguments() {
        return mAdapterArguments;
    }

    public void setAdapterArguments(List<String> adapterArguments) {
        mAdapterArguments = adapterArguments;
    }

    public String getAdapterName() {
        return mAdapterName;
    }

    public void setAdapterName(String adapterName) {
        mAdapterName = adapterName;
    }

    @Override
    public String toString() {
        return "AgentParams{" + "mApplyingPackage=" + mApplyingPackage + ", mAdapterArguments=" + mAdapterArguments + ", mAdapterName='" + mAdapterName + '\'' + '}';
    }

}
