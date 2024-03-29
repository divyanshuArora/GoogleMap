package com.example.googlemap.ApiResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearByPlaces_Response {

        @SerializedName("html_attributions")
        @Expose
        public List<Object> htmlAttributions = null;
        @SerializedName("next_page_token")
        @Expose
        public String nextPageToken;
        @SerializedName("results")
        @Expose
        public List<Result> results = null;
        @SerializedName("status")
        @Expose
        public String status;

        public List<Object> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<Object> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public String getNextPageToken() {
            return nextPageToken;
        }

        public void setNextPageToken(String nextPageToken) {
            this.nextPageToken = nextPageToken;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }



    public class Geometry {

        @SerializedName("location")
        @Expose
        public Location location;
        @SerializedName("viewport")
        @Expose
        public Viewport viewport;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

    }

    public class Location {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;

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


    public class Northeast {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;

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


    public class OpeningHours {

        @SerializedName("open_now")
        @Expose
        public String  openNow;

        public String getOpenNow() {
            return openNow;
        }

        public void setOpenNow(String openNow) {
            this.openNow = openNow;
        }

    }


    public class Photo {

        @SerializedName("height")
        @Expose
        public Integer height;
        @SerializedName("html_attributions")
        @Expose
        public List<String> htmlAttributions = null;
        @SerializedName("photo_reference")
        @Expose
        public String photoReference;
        @SerializedName("width")
        @Expose
        public Integer width;

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public List<String> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<String> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }

    public class PlusCode {

        @SerializedName("compound_code")
        @Expose
        public String compoundCode;
        @SerializedName("global_code")
        @Expose
        public String globalCode;

        public String getCompoundCode() {
            return compoundCode;
        }

        public void setCompoundCode(String compoundCode) {
            this.compoundCode = compoundCode;
        }

        public String getGlobalCode() {
            return globalCode;
        }

        public void setGlobalCode(String globalCode) {
            this.globalCode = globalCode;
        }

    }

    public class Result {

        @SerializedName("geometry")
        @Expose
        public Geometry geometry;
        @SerializedName("icon")
        @Expose
        public String icon;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("opening_hours")
        @Expose
        public OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        public List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        public String placeId;
        @SerializedName("plus_code")
        @Expose
        public PlusCode plusCode;
        @SerializedName("rating")
        @Expose
        public String rating;
        @SerializedName("reference")
        @Expose
        public String reference;
        @SerializedName("scope")
        @Expose
        public String scope;
        @SerializedName("types")
        @Expose
        public List<String> types = null;
        @SerializedName("user_ratings_total")
        @Expose
        public String userRatingsTotal;
        @SerializedName("vicinity")
        @Expose
        public String vicinity;
        @SerializedName("price_level")
        @Expose
        public Integer priceLevel;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public PlusCode getPlusCode() {
            return plusCode;
        }

        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getUserRatingsTotal() {
            return userRatingsTotal;
        }

        public void setUserRatingsTotal(String userRatingsTotal) {
            this.userRatingsTotal = userRatingsTotal;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public Integer getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(Integer priceLevel) {
            this.priceLevel = priceLevel;
        }

    }


    public class Southwest {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;

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


    public class Viewport {

        @SerializedName("northeast")
        @Expose
        public Northeast northeast;
        @SerializedName("southwest")
        @Expose
        public Southwest southwest;

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }



}
