package bd.com.universal.eparking.seeker.PoJoClasses;

/**
 * Created by Nipon on 9/18/2018.
 */

public class Vehicle {
    private String mVehicleNumber;
    private String mVehicleType;
    private String mBlueBookImage;

    public Vehicle() {

    }

    public Vehicle(String mVehicleNumber, String mVehicleType, String mBlueBookImage) {
        this.mVehicleNumber = mVehicleNumber;
        this.mVehicleType = mVehicleType;
        this.mBlueBookImage = mBlueBookImage;
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
}
