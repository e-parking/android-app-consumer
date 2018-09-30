package bd.com.universal.eparking.seeker.PoJoClasses;

/**
 * Created by Nipon on 9/18/2018.
 */

public class Vehicle {
    private String mVehicleNumberPrefix;
    private String mVehicleNumber;
    private String mVehicleType;
    private String mBlueBookImage;
    private String mVehicleImage;


    public Vehicle() {

    }

    public Vehicle(String mVehicleNumberPrefix,String mVehicleNumber, String mVehicleType, String mBlueBookImage,String mVehicleImage) {
        this.mVehicleNumberPrefix = mVehicleNumberPrefix;
        this.mVehicleNumber = mVehicleNumber;
        this.mVehicleType = mVehicleType;
        this.mBlueBookImage = mBlueBookImage;
        this.mVehicleImage = mVehicleImage;
    }


    public String getmVehicleNumberPrefix() {
        return mVehicleNumberPrefix;
    }

    public void setmVehicleNumberPrefix(String mVehicleNumberPrefix) {
        this.mVehicleNumberPrefix = mVehicleNumberPrefix;
    }

    public String getmVehicleNumber() {
        return mVehicleNumber;
    }

    public void setmVehicleNumber(String mVehicleNumber) {
        this.mVehicleNumber = mVehicleNumber;
    }

    public String getmVehicleType() {
        return mVehicleType;
    }

    public void setmVehicleType(String mVehicleType) {
        this.mVehicleType = mVehicleType;
    }

    public String getmBlueBookImage() {
        return mBlueBookImage;
    }

    public void setmBlueBookImage(String mBlueBookImage) {
        this.mBlueBookImage = mBlueBookImage;
    }

    public String getmVehicleImage() {
        return mVehicleImage;
    }

    public void setmVehicleImage(String mVehicleImage) {
        this.mVehicleImage = mVehicleImage;
    }
}
