package bd.com.universal.eparking.seeker.WebApis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResponse {
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    public static class Route {


        @SerializedName("legs")
        @Expose
        private List<Leg> legs = null;


        public List<Leg> getLegs() {
            return legs;
        }
        public void setLegs(List<Leg> legs) {
            this.legs = legs;
        }


    }
    public static class Leg {


        @SerializedName("steps")
        @Expose
        private List<Step> steps = null;

        public List<Step> getSteps() {
            return steps;
        }
        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }
    }
    public static class Step {
        @SerializedName("end_location")
        @Expose
        private EndLocation_ endLocation;

        @SerializedName("start_location")
        @Expose
        private StartLocation_ startLocation;
        @SerializedName("travel_mode")
        @Expose
        private String travelMode;
        @SerializedName("maneuver")
        @Expose
        private String maneuver;


        public EndLocation_ getEndLocation() {
            return endLocation;
        }
        public void setEndLocation(EndLocation_ endLocation) {
            this.endLocation = endLocation;
        }

        public StartLocation_ getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(StartLocation_ startLocation) {
            this.startLocation = startLocation;
        }
        public String getTravelMode() {
            return travelMode;
        }
        public void setTravelMode(String travelMode) {
            this.travelMode = travelMode;
        }
        public String getManeuver() {
            return maneuver;
        }
        public void setManeuver(String maneuver) {
            this.maneuver = maneuver;
        }
    }
    public class StartLocation_ {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
    public class EndLocation_ {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
}
