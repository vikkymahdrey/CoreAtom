package com.agiledge.atom.dto;

public class RouteDto {
	private String area;
	private String place;
	private String landmark;
	private String areaId;
	private String placeId;
	private String LandmarkId;
	private int routeId;
	private String distance;
	private String actualDistance;
	private String siteId;
	private String siteName;
	private String position;
	private String routeName;
	private String routeType;
	private String routeTypeDesc;
	private String lattitude;
	private String longitude;
	private String doneBy;
	private String vehicleType;
	private String vehicleTypeId;
	private String vehicleCount;
	private int seat;
	private String seats;
	private String bookingCount;
	private String inOut;
	private String time;
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleCount() {
		return vehicleCount;
	}

	public void setVehicleCount(String vehicleCount) {
		this.vehicleCount = vehicleCount;
	}

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public String getBookingCount() {
		return bookingCount;
	}

	public void setBookingCount(String bookingCount) {
		this.bookingCount = bookingCount;
	}

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getLandmarkId() {
		return LandmarkId;
	}

	public void setLandmarkId(String landmarkId) {
		LandmarkId = landmarkId;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getActualDistance() {
		return actualDistance;
	}

	public void setActualDistance(String actualDistance) {
		this.actualDistance = actualDistance;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getRouteTypeDesc() {
		return routeTypeDesc;
	}

	public void setRouteTypeDesc(String routeTypeDesc) {
		switch (routeTypeDesc) {
		case "p":
			this.routeTypeDesc = "Primary";
			break;
		case "c":
			this.routeTypeDesc = "Combined";
			break;
		case "f":
			this.routeTypeDesc = "Full Combined";
			break;
		default:
			this.routeTypeDesc = routeTypeDesc;
		}
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getInOut() {
		return inOut;
	}

	public void setInOut(String inOut) {
		this.inOut = inOut;
	}

	public String getVehicleTypeId() {
		return vehicleTypeId;
	}

	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
}
