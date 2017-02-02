package com.agiledge.atom.dto;

public class TripCommentDto {

	 
		private String tripId;
		private String commentedById;
		private String commentedByName;
		private String commentedDate;
		private String comment;
		
		public String toString()
		{
			return "TripId :" + tripId + ", Comment :" + comment + ", commentedByName: " + commentedByName +"("+commentedById+")" + " date : " + commentedDate;
			
		}
		
		public String getTripId() {
			return tripId;
		}
		public void setTripId(String tripId) {
			this.tripId = tripId;
		}
		public String getCommentedById() {
			return commentedById;
		}
		public void setCommentedById(String commentedById) {
			this.commentedById = commentedById;
		}
		public String getCommentedByName() {
			return commentedByName;
		}
		public void setCommentedByName(String commentedByName) {
			this.commentedByName = commentedByName;
		}
		public String getCommentedDate() {
			return commentedDate;
		}
		public void setCommentedDate(String commentedDate) {
			this.commentedDate = commentedDate;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}

	

}
