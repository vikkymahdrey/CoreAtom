package accessRight.dto;

public class AccessRightDto {
 
	
	private String accessRightName;
	private int accessRightId;
	private String userType;
	private int roleId;
	private String description;

	public String getAccessRightName() {
		return accessRightName;
	}
	public void setAccessRightName(String accessRightName) {
		this.accessRightName = accessRightName;
	}
	public int getAccessRightId() {
		return accessRightId;
	}
	public void setAccessRightId(int accessRightId) {
		this.accessRightId = accessRightId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
