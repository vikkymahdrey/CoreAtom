package com.agiledge.atom.hibernate.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name="dds")
@Table(name="adhoctrips")
public class AdhocTripHBMDto {

	@Override
	public String toString() {
		return "AdhocTripHBMDto [tripId=" + tripId + ", vendorId=" + vendorId
				+ "]";
	}
	private long tripId;

	@Id
	@GeneratedValue
	@Column(name="id", nullable=false)
	public long getTripId() {
		return tripId;
	}
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
	@Column(name="vendorId", nullable=true)
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	private String vendorId;
}
