package bd.com.universal.eparking.seeker.PoJoClasses;

/**
 * Created by Nipon on 9/17/2018.
 */

public class ProviderRating {
    private float mConsumerRatingValue;
    private String mStatus;

    public ProviderRating(float mConsumerRatingValue,String mStatus) {
        this.mConsumerRatingValue = mConsumerRatingValue;
        this.mStatus=mStatus;
    }

    public ProviderRating() {
    }

    public float getmConsumerRatingValue() {
        return mConsumerRatingValue;
    }

    public void setmConsumerRatingValue(float mConsumerRatingValue) {
        this.mConsumerRatingValue = mConsumerRatingValue;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
