package com.agiledge.atom.loadsettings.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.agiledge.atom.loadsettings.dao.LoadSettingsDao;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;

public class LoadSettingsService {

	public void addPages() {

		String pages[][] = {

				{ "transportmanager_home.jsp", "PAGE" },
				{ "employee_home.jsp", "PAGE" },
				{ "manager_home.jsp", "PAGE" },
				{ ".#getproject.jsp.1.6", "PAGE" },
				{ ".#schedule_modify_all.jsp.1.29", "PAGE" },
				{ ".#transadmin_schedulemodify.jsp.1.24", "PAGE" },
				{ "addCompany.jsp", "PAGE" },
				{ "addExternalemp.jsp", "PAGE" },
				{ "AddroleView.jsp", "PAGE" },
				{ "AddSubview.jsp", "PAGE" },
				{ "AddView.jsp", "PAGE" },
				{ "add_route.jsp", "PAGE" },
				{ "AllView.jsp", "PAGE" },
				{ "area.jsp", "PAGE" },
				{ "Atom_Help.pps", "PAGE" },
				{ "automatic_routing.jsp", "PAGE" },
				{ "board_tripsheet.jsp", "PAGE" },
				{ "branch.jsp", "PAGE" },
				{ "capacityutilizationdashboard.jsp", "PAGE" },
				{ "changePassword.jsp", "PAGE" },
				{ "ChangeRole.jsp", "PAGE" },
				{ "compare_trips.jsp", "PAGE" },
				{ "dashboard.jsp", "PAGE" },
				{ "delegateRole.jsp", "PAGE" },
				{ "delegateRoleModify.jsp", "PAGE" },
				{ "display_actualTrips.jsp", "PAGE" },
				{ "display_AgileBillingReport.jsp", "PAGE" },
				{ "display_billing_breakups.jsp", "PAGE" },
				{ "display_CapacityUtilizationReport.jsp", "PAGE" },
				{ "display_CapacityUtilizationSummaryReport.jsp", "PAGE" },
				{ "display_employeeSubscriptionReport.jsp", "PAGE" },
				{ "display_emp_sch_report.jsp", "PAGE" },
				{ "display_emp_sub_report.jsp", "PAGE" },
				{ "display_emp_unsub_report.jsp", "PAGE" },
				{ "display_exceptionReport.jsp", "PAGE" },
				{ "display_manager_projectWiseTripBillingReport.jsp", "PAGE" },
				{ "display_OnShowTipCount.jsp", "PAGE" },
				{ "display_OTA_OTD.jsp", "PAGE" },
				{ "display_projectWiseTripBillingReport.jsp", "PAGE" },
				{ "display_TrackedTripBucket.jsp", "PAGE" },
				{ "display_tranadmin_emp_sch_report.jsp", "PAGE" },
				{ "display_transadmin_tripSheet_no_show_count_report.jsp",
						"PAGE" }, { "display_TripBillingSummary.jsp", "PAGE" },
				{ "display_tripSheet_no_show_count_report.jsp", "PAGE" },
				{ "downloadapl.jsp", "PAGE" },
				{ "downloadnotschedule.jsp", "PAGE" },
				{ "downloadPayroll.jsp", "PAGE" },
				{ "downloadroute.jsp", "PAGE" },
				{ "download_display_billing_breakups.jsp", "PAGE" },
				{ "drilldownReport.jsp", "PAGE" },
				{ "editSubview.jsp", "PAGE" }, { "editView.jsp", "PAGE" },
				{ "edit_route.jsp", "PAGE" }, { "EmployeeSearch.jsp", "PAGE" },
				{ "employeesearchaddress.jsp", "PAGE" },
				{ "emp_modifySubscription.jsp", "PAGE" },
				{ "emp_schedulecancel.jsp", "PAGE" },
				{ "emp_schedule_report.jsp", "PAGE" },
				{ "emp_schedulingHistory.jsp", "PAGE" },
				{ "emp_showSubscription.jsp", "PAGE" },
				{ "emp_subscription.jsp", "PAGE" },
				{ "emp_subscriptionHistory.jsp", "PAGE" },
				{ "emp_subscription_report.jsp", "PAGE" },
				{ "emp_unsubscriptionRequest.jsp", "PAGE" },
				{ "emp_unsubscription_report.jsp", "PAGE" },
				{ "error.jsp", "PAGE" },
				{ "ExternalEmpTemplate.xlsx", "PAGE" },
				{ "Footer.jsp", "PAGE" }, { "getemployee.jsp", "PAGE" },
				{ "getproject.jsp", "PAGE" },
				{ "get_site_vehicle.jsp", "PAGE" }, { "Header.jsp", "PAGE" },
				{ "hrm_Header.jsp", "PAGE" }, { "index.jsp", "PAGE" },
				{ "landmark.jsp", "PAGE" }, { "LandMarkSearch.jsp", "PAGE" },
				{ "logtime_list.jsp", "PAGE" }, { "log_time.jsp", "PAGE" },
				{ "log_time_modify.jsp", "PAGE" },
				{ "modifyDelegation.jsp", "PAGE" },
				{ "modify_trip.jsp", "PAGE" },
				{ "NoShowdashboard.jsp", "PAGE" },
				{ "OTAOTDDashboard.jsp", "PAGE" },
				{ "payrolldetails.jsp", "PAGE" },
				{ "payroll_setup.jsp", "PAGE" }, { "place.jsp", "PAGE" },
				{ "print_trips.jsp", "PAGE" },
				{ "projectwisenoshowdashboard.jsp", "PAGE" },
				{ "rolepage.jsp", "PAGE" }, { "roleView.jsp", "PAGE" },
				{ "routeSetup.jsp", "PAGE" }, { "route_list.jsp", "PAGE" },
				{ "route_modify.jsp", "PAGE" },
				{ "scheduled_employee.jsp", "PAGE" },
				{ "schedule_alter.jsp", "PAGE" },
				{ "schedule_employee.jsp", "PAGE" },
				{ "schedule_modify.jsp", "PAGE" },
				{ "schedule_modify_all.jsp", "PAGE" },
				{ "SearchEmployeeAndSetSubscriptionDetails.jsp", "PAGE" },
				{ "setSpecial.jsp", "PAGE" }, { "ShowAuditLog.jsp", "PAGE" },
				{ "site.jsp", "PAGE" }, { "site_shift.jsp", "PAGE" },
				{ "site_vehicle_list.jsp", "PAGE" },
				{ "site_vehicle_type.jsp", "PAGE" },
				{ "SupervisorSearch1.jsp", "PAGE" },
				{ "SupervisorSearch2.jsp", "PAGE" },
				{ "termsAndConditions.html", "PAGE" }, { "Test.jsp", "PAGE" },
				{ "Test1.html", "PAGE" }, { "Tomorrows.jsp", "PAGE" },
				{ "trackedTripDownloads.jsp", "PAGE" },
				{ "transadminviewDelegatedRole.jsp", "PAGE" },
				{ "transadmin_approvedTrips.jsp", "PAGE" },
				{ "transadmin_delegateRole.jsp", "PAGE" },
				{ "transadmin_modifysubscription.jsp", "PAGE" },
				{ "transadmin_scheduledemployee.jsp", "PAGE" },
				{ "transadmin_schedulemodify.jsp", "PAGE" },
				{ "transadmin_schedule_alter.jsp", "PAGE" },
				{ "transadmin_schedule_employee.jsp", "PAGE" },
				{ "transadmin_subscriptioncancel.jsp", "PAGE" },
				{ "transadmin_subscriptionforemp.jsp", "PAGE" },
				{ "transadmin_trackedTrips.jsp", "PAGE" },
				{ "transadmin_vendorSetUp.jsp", "PAGE" },
				{ "transadmin_viewschedule.jsp", "PAGE" },
				{ "transadmin_viewTrackedTripSheet.jsp", "PAGE" },
				{ "tripAuditLog.jsp", "PAGE" },
				{ "TripCostCalandar.jsp", "PAGE" },
				{ "trip_details.jsp", "PAGE" },
				{ "trip_details_modify.jsp", "PAGE" },
				{ "UnitDataTemplate.xlsx", "PAGE" },
				{ "UnituploadExcel.jsp", "PAGE" },
				{ "uploadExcel.jsp", "PAGE" }, { "upload_route.jsp", "PAGE" },
				{ "urlAssign.jsp", "PAGE" }, { "urlSetup.jsp", "PAGE" },
				{ "userRoleSetUp.jsp", "PAGE" },
				{ "VehicleMixdashboard.jsp", "PAGE" },
				{ "vehicle_type.jsp", "PAGE" },
				{ "vehicle_type_list.jsp", "PAGE" },
				{ "vehicle_type_modify.jsp", "PAGE" },
				{ "vendors_home.jsp", "PAGE" },
				{ "Vendor_Header.jsp", "PAGE" }, { "vendor_site.jsp", "PAGE" },
				{ "vendor_tripApproval.jsp", "PAGE" },
				{ "vendor_trip_details.jsp", "PAGE" },
				{ "vendor_viewTripSheet.jsp", "PAGE" },
				{ "viewallExternal.jsp", "PAGE" },
				{ "viewDelegatedRole.jsp", "PAGE" },
				{ "viewSettings.jsp", "PAGE" }, { "ViewSubview.jsp", "PAGE" },
				{ "viewUnit.jsp", "PAGE" }, { "view_all_special.jsp", "PAGE" },
				{ "view_routing.jsp", "PAGE" },
				{ "view_tracked_trip.jsp", "PAGE" }, { "Common", "SERVLET" },
				{ "Main", "SERVLET" }, { "OtherFunctions", "SERVLET" },
				{ "PasswordGenerator", "SERVLET" },
				{ "PropertyClass", "SERVLET" },
				{ "AuditLogConstants", "SERVLET" },
				{ "BranchDao$addEmployee", "SERVLET" },
				{ "CompanyDao$addEmployee", "SERVLET" },
				{ "OtherDao$OtherFunctionsHolder", "SERVLET" },
				{ "RouteUpload", "SERVLET" }, { "SettingsDoa", "SERVLET" },
				{ "ValidateUser", "SERVLET" }, { "DbConnect$1", "SERVLET" },
				{ "DbConnect", "SERVLET" },
				{ "EmployeeSubscriptionDto$status$1", "SERVLET" },
				{ "EmployeeSubscriptionDto$status$2", "SERVLET" },
				{ "EmployeeSubscriptionDto$status$3", "SERVLET" },
				{ "EmployeeSubscriptionDto$status", "SERVLET" },
				{ "PayrollReportDto$DetailClass", "SERVLET" },
				{ "Check", "SERVLET" }, { "SendGmail$1", "SERVLET" },
				{ "SendGmail", "SERVLET" }, { "SendMail", "SERVLET" },
				{ "SendMailFactory", "SERVLET" },
				{ "SendMailLogica", "SERVLET" }, { "SendMailUAT", "SERVLET" },
				{ "SendSMS", "SERVLET" }, { "Export2PDF", "SERVLET" },
				{ "ActualCapacity", "SERVLET" },
				{ "ActualCapacityServlet", "SERVLET" },
				{ "DailyCapacityUtilization", "SERVLET" },
				{ "DailyCapacityUtilizationServlet", "SERVLET" },
				{ "DailyNoShow", "SERVLET" },
				{ "DailyNoShowServlet", "SERVLET" },
				{ "DailyVehicleMixServlet", "SERVLET" },
				{ "NoShowbyProjectsServlet", "SERVLET" }, { "OTA", "SERVLET" },
				{ "OTAServlet", "SERVLET" }, { "OTD", "SERVLET" },
				{ "OTDServlet", "SERVLET" }, { "PerPersonCost", "SERVLET" },
				{ "PerPersonCostServlet", "SERVLET" },
				{ "PlannedActualCapacity", "SERVLET" },
				{ "PlannedCapacity", "SERVLET" },
				{ "ProjectwiseNoShow", "SERVLET" },
				{ "ProjectwiseNoShowServlet", "SERVLET" },
				{ "ShiftCapacityUtilization", "SERVLET" },
				{ "ShiftCapacityUtilizationServlet", "SERVLET" },
				{ "ShiftNoShow", "SERVLET" },
				{ "ShiftNoShowServlet", "SERVLET" },
				{ "SiteCostSummary", "SERVLET" },
				{ "SiteCostSummaryServlet", "SERVLET" },
				{ "SiteNoShow", "SERVLET" },
				{ "SiteNoShowServlet", "SERVLET" },
				{ "VehicleMix", "SERVLET" },
				{ "VehicleMixServlet", "SERVLET" },
				{ "VendorCost", "SERVLET" },
				{ "VendorCostServlet", "SERVLET" },
				{ "EmpSchedule", "SERVLET" },
				{ "EmpSubDataSource", "SERVLET" },
				{ "EmpSubscription", "SERVLET" },
				{ "EmpUnsubscription", "SERVLET" },
				{ "EmpScheduleReportHelper", "SERVLET" },
				{ "EmpSubscriptionReportHelper", "SERVLET" },
				{ "EmpUnSubscriptionReportHelper", "SERVLET" },
				{ "SchedulingReport", "SERVLET" },
				{ "TripBillingBreakUpsReportHelper", "SERVLET" },
				{ "TripBucketReportHelper", "SERVLET" },
				{ "TripSheetNoShowCountReportHelper", "SERVLET" },
				{ "PayrollService$1", "SERVLET" }, { "Test", "SERVLET" },
				{ "AddCompany", "SERVLET" }, { "AddProjectToTime", "SERVLET" },
				{ "AddRoute", "SERVLET" }, { "AddSettings", "SERVLET" },
				{ "AddTripRate", "SERVLET" }, { "AddUnit", "SERVLET" },
				{ "AddVendor", "SERVLET" }, { "AddVendorToSite", "SERVLET" },
				{ "AddArea", "SERVLET" }, { "AddLandmark", "SERVLET" },
				{ "AddPlace", "SERVLET" }, { "GetLandMark", "SERVLET" },
				{ "Pre", "SERVLET" }, { "UpdateArea", "SERVLET" },
				{ "UpdateLandmark", "SERVLET" }, { "UpdatePlace", "SERVLET" },
				{ "ApproveTrips", "SERVLET" }, { "Branch", "SERVLET" },
				{ "ChangeUser", "SERVLET" }, { "CheckData", "SERVLET" },
				{ "DelegateRole", "SERVLET" },
				{ "DeleteLogModify", "SERVLET" },
				{ "DeleteSettings", "SERVLET" }, { "deleteVendor", "SERVLET" },
				{ "EditEmployee", "SERVLET" }, { "EditUnit", "SERVLET" },
				{ "AddExternalEmployee", "SERVLET" },
				{ "ChangePassword", "SERVLET" }, { "CheckLoginId", "SERVLET" },
				{ "GetEmployee", "SERVLET" }, { "GetEmployeeData", "SERVLET" },
				{ "GetEmployeeDetails", "SERVLET" },
				{ "GetEmployeeForManagerData", "SERVLET" },
				{ "GetEmployeeView", "SERVLET" },
				{ "GetSubscribedEmployeeData", "SERVLET" },
				{ "UpdateEmployeesRole", "SERVLET" },
				{ "EmployeeScheduleCancel", "SERVLET" },
				{ "Export2PDFServlet", "SERVLET" },
				{ "GetEmployeeAddress", "SERVLET" },
				{ "GetEmployeeSpecial", "SERVLET" },
				{ "GetLogTime", "SERVLET" }, { "GetProject", "SERVLET" },
				{ "GetProjectSpecificShiftTime_Ajax", "SERVLET" },
				{ "GetSettings", "SERVLET" }, { "GetSiteShift", "SERVLET" },
				{ "GetUnits", "SERVLET" },
				{ "GetVehicleNotInSite", "SERVLET" },
				{ "IsWithinSchedules", "SERVLET" }, { "Logout", "SERVLET" },
				{ "LogTime", "SERVLET" }, { "LogTimeModify", "SERVLET" },
				{ "ModifyDelegateRole", "SERVLET" },
				{ "ModifyMasterRoute", "SERVLET" },
				{ "ModifyRoute", "SERVLET" }, { "NewClass", "SERVLET" },
				{ "GetPayrollDetails", "SERVLET" },
				{ "removeAssignment", "SERVLET" },
				{ "ResetPassword", "SERVLET" }, { "Routing", "SERVLET" },
				{ "SaveTrip", "SERVLET" }, { "ScheduleAlter", "SERVLET" },
				{ "ScheduleEmployee", "SERVLET" },
				{ "ScheduleModify", "SERVLET" },
				{ "ScheduleModifyCancel", "SERVLET" },
				{ "SetGetRolePage", "SERVLET" }, { "SetRouteRule", "SERVLET" },
				{ "SetSiteShift", "SERVLET" }, { "SetSpecial", "SERVLET" },
				{ "AddSite", "SERVLET" }, { "UpdateSite", "SERVLET" },
				{ "SiteVehicle", "SERVLET" }, { "Startup", "SERVLET" },
				{ "GetEmployeeDataFromSubscription", "SERVLET" },
				{ "GetLastSubscriptionDetails_Ajax", "SERVLET" },
				{ "ModifySubscribeRequest", "SERVLET" },
				{ "Subscribe", "SERVLET" },
				{ "SubscriptionSelector", "SERVLET" },
				{ "UnsubscriptionRequest", "SERVLET" },
				{ "UnsubscriptionSelector", "SERVLET" },
				{ "UpdateSubscription", "SERVLET" }, { "Test", "SERVLET" },
				{ "TrackTripSheet", "SERVLET" },
				{ "UnitUploadExcel", "SERVLET" },
				{ "UpdatePayrollAmount", "SERVLET" },
				{ "UpdateStatus", "SERVLET" }, { "UpdateTrip", "SERVLET" },
				{ "Updateunit", "SERVLET" }, { "UpdateVendor", "SERVLET" },
				{ "UploadExcel", "SERVLET" }, { "UserValidation", "SERVLET" },
				{ "VehicleType", "SERVLET" },
				{ "VehicleTypeModify", "SERVLET" },
				{ "VendorContract", "SERVLET" },
				{ "SettingStatus", "SERVLET" }, { "GetEmps", "SERVLET" },
				{ "GetProject", "SERVLET" }, { "ConnectDB", "SERVLET" },
				{ "Scheduler", "SERVLET" }, { "UtilityServices", "SERVLET" },
				{ "AddPage", "SERVLET" }, { "AddSubview", "SERVLET" },
				{ "AddUserRole", "SERVLET" }, { "AddView", "SERVLET" },
				{ "AddViewRole", "SERVLET" },
				{ "CheckUserNameExists", "SERVLET" },
				{ "CheckUserTypeExists", "SERVLET" },
				{ "DeleteSubview", "SERVLET" }, { "DeleteView", "SERVLET" },
				{ "DeleteViewRole", "SERVLET" }, { "EditPage", "SERVLET" },
				{ "EditSubview", "SERVLET" }, { "EditView", "SERVLET" },
				{ "GetAssignedUrls", "SERVLET" }, { "GroupUrls", "SERVLET" },
				{ "SetRolesHomePage", "SERVLET" },
				{ "UpdateUserRole", "SERVLET" },
				{ "PageManagementStatus", "SERVLET" },
				{ "ViewManagementStatus", "SERVLET" },
				{ "transporttype_mapping.jsp", "PAGE" }, { "#", "PAGE" } };

		ArrayList<PageDto> dtoList = new ArrayList<PageDto>();
		for (int i = 0; i < pages.length; i++) {

			PageDto dto = templateToPageDtoForLoadPage_Default(pages[i]);
			dtoList.add(dto);

		}

		new LoadSettingsDao().loadPage_Default(dtoList);

	}

	public void setHomePage_Default() {

		String pages[][] = { { "transportmanager_home.jsp", "tm" },
				{ "transportmanager_home.jsp", "admin" },
				{ "manager_home.jsp", "hrm" } };

		ArrayList<PageDto> dtoList = new ArrayList<PageDto>();
		for (int i = 0; i < pages.length; i++) {

			PageDto dto = templateToPageDtoForSetHomePage_Default(pages[i]);
			dtoList.add(dto);

		}

		new LoadSettingsDao().setHomePage_Default(dtoList);

	}

	public void loadViews_Default() {

		String views[][] = {
				{ "HOME", "HOME-ta", "1", "transportmanager_home.jsp" },
				{ "CANCEL SCHEDULE", "CANCEL SCHEDULE", "15",
						"emp_schedulecancel.jsp" },
				{ "SCHEDULES", "SCHEDULES", "14", "emp_subscriptionHistory.jsp" },
				{ "UNSUBSCRIBE", "UNSUBSCRIBE", "13",
						"emp_unsubscriptionRequest.jsp" },
				{ "VIEW SCHEDULE", "emp schedule view", "3",
						"scheduled_employee.jsp" },
				{ "CREATE SCHEDULE", "CREATE SCHEDULE", "2",
						"schedule_employee.jsp" },
				{ "MODIFY SCHEDULE", "hrm scheudle modify", "4",
						"schedule_modify_all.jsp" },
				{ "HOME", "HOME-vendor home", "16", "vendors_home.jsp" },
				{ "TRACK TRIP", "TRACK TRIP", "17", "vendor_viewTripSheet.jsp" },
				{ "LOGOUT", "LOGOUT", "9", "Logout" },
				{ "SUBSCRIBE", "SUBSCRIBE", "12", "SubscriptionSelector" },
				{ "SETTINGS", "SETTINGS", "2", "#" },
				{ "ROUTE MASTER", "ROUTE MASTER", "3", "#" },
				{ "ROUTING", "ROUTING", "4", "#" },
				{ "FOR EMPLOYEES", "FOR EMPLOYEES", "5", "#" },
				{ "REPORTS", "REPORTS", "6", "#" },
				{ "OTHERS", "OTHERS", "7", "#" },
				{ "User Mgmt", "User Mgmt", "8", "#" },
				{ "ForEMP", "ForEMP", "6", "#" },
				{ "Admin", "Admin", "1", "#" },
				{ "Billing Setup", "Billing Setup", "1", "#" }

		};

		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		for (int i = 0; i < views.length; i++) {

			ViewManagementDto dto = templateToPageDtoForLoadViews_Default(views[i]);
			dtoList.add(dto);

		}

		new LoadSettingsDao().loadViews_Default(dtoList);

	}

	public void loadSubViews_Default() {

		String views[][] = {
				{ "COMPANY SETUP", "COMPANY SETUP", "1", "addCompany.jsp",
						"SETTINGS" },
				{ "ADD ROUTE", "ADD ROUTE", "2", "add_route.jsp",
						"ROUTE MASTER" },
				{ "Views", "Views", "2", "AllView.jsp", "User Mgmt" },
				{ "Menu Setup", "Menu Setup", "2", "AllView.jsp", "Admin" },
				{ "APL CONFIG", "APL CONFIG", "1", "area.jsp", "ROUTE MASTER" },
				{ "GENERATE TRIPSHEET", "GENERATE TRIPSHEET", "1",
						"automatic_routing.jsp", "ROUTING" },
				{ "ASSIGN ROLE", "ASSIGN ROLE", "1", "ChangeRole.jsp",
						"FOR EMPLOYEES" },
				{ "ADD DELEGATION", "ADD DELEGATION", "3", "delegateRole.jsp",
						"SETTINGS" },
				{ "CAPACITY UTILIZATION", "CAPACITY UTILIZATION", "9",
						"display_CapacityUtilizationSummaryReport.jsp",
						"REPORTS" },
				{ "SUBSCRIPTION", "EMPLOYEE-SUBSCRIPTION REPORT", "2",
						"display_employeeSubscriptionReport.jsp", "REPORTS" },
				{ "SCHEDULING", "SCHEDULING REPORT", "3",
						"display_emp_sch_report.jsp", "REPORTS" },
				{ "UN-SUBSCRIPTION", "UN-SUBSCRIPTION", "4",
						"display_emp_unsub_report.jsp", "REPORTS" },
				{ "EXCEPTION", "EXCEPTION", "10",
						"display_exceptionReport.jsp", "REPORTS" },
				{ "OTA & OTD", "OTA & OTD", "6", "display_OnShowTipCount.jsp",
						"REPORTS" },
				{ "PROJECT WISE COST", "PROJECT WISE COST", "8",
						"display_projectWiseTripBillingReport.jsp", "REPORTS" },
				{ "TRACKED TRIPS", "TRACKED TRIPS", "5",
						"display_TrackedTripBucket.jsp", "ROUTING" },
				{ "TRIP BILLING", "TRIP BILLING", "7",
						"display_TripBillingSummary.jsp", "REPORTS" },
				{ "NO SHOW", "NO SHOW", "5",
						"display_tripSheet_no_show_count_report.jsp", "REPORTS" },
				{ "SHIFT TIME", "SHIFT TIME", "5", "logtime_list.jsp",
						"SETTINGS" },
				{ "PAYROLL", "PAYROLL", "1", "payrolldetails.jsp", "REPORTS" },
				{ "ROUTE SETUP", "ROUTE SETUP", "4", "routeSetup.jsp",
						"ROUTE MASTER" },
				{ "VIEW ROUTE", "VIEW ROUTE", "3", "route_list.jsp",
						"ROUTE MASTER" },
				{ "SITE SHIFT", "SITE SHIFT", "5", "site_shift.jsp", "SETTINGS" },
				{ "ASSIGN VEHICLE", "ASSIGN VEHICLE", "8",
						"site_vehicle_type.jsp", "SETTINGS" },
				{ "MODIFY SUBSCRIPTION", "MODIFY SUBSCRIPTION", "2",
						"transadmin_modifysubscription.jsp", "FOR EMPLOYEES" },
				{ "VIEW SCHEDULE", "tm-schedule view", "4",
						"transadmin_scheduledemployee.jsp", "FOR EMPLOYEES" },
				{ "MODIFY SCHEDULE", "tm schedule modify", "5",
						"transadmin_schedulemodify.jsp", "FOR EMPLOYEES" },
				{ "SCHEDULING", "tm-schedule", "3",
						"transadmin_schedule_employee.jsp", "FOR EMPLOYEES" },
				{ "Subscription", "tm-SUBSCRIPTION", "2",
						"transadmin_subscriptionforemp.jsp", "FOR EMPLOYEES" },
				{ "VENDOR SETUP", "VENDOR SETUP", "2",
						"transadmin_vendorSetUp.jsp", "SETTINGS" },
				{ "ROLE SETUP", "ROLE SETUP", "1", "userRoleSetUp.jsp", "Admin" },
				{ "Map Transport type", "map transport type", "1",
						"transporttype_mapping.jsp", "Billing Setup" },

				{ "ADD VEHICLE TYPE", "ADD VEHICLE TYPE", "10",
						"vehicle_type.jsp", "SETTINGS" },
				{ "VIEW VEHICLE TYPE", "VIEW VEHICLE TYPE", "7",
						"vehicle_type_list.jsp", "SETTINGS" },
				{ "VIEW DELEGATION", "VIEW DELEGATION", "4",
						"viewDelegatedRole.jsp", "SETTINGS" },
				{ "VIEW TRIPS", "VIEW TRIPS", "3", "view_routing.jsp",
						"ROUTING" },
				{ "CHANGE EMPLOYEE", "CHANGE EMPLOYEE", "2", "ChangeUser",
						"OTHERS" },
				{ "subscription", "EMPLOYEE-SUBSCRIPTION", "1",
						"SubscriptionSelector", "ForEMP" } };

		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		for (int i = 0; i < views.length; i++) {

			ViewManagementDto dto = templateToPageDtoForLoadSubViews_Default(views[i]);
			dtoList.add(dto);

		}

		new LoadSettingsDao().loadSubViews_Default(dtoList);

	}

	public void loadRolesViewsAssociation_Default() {

		String views[][] = { { "emp", "HOME" }, { "emp", "LOGOUT" },
				{ "emp", "SUBSCRIBE" }, { "emp", "UNSUBSCRIBE" },
				{ "emp", "SCHEDULES" }, { "emp", "CANCEL SCHEDULE" },
				{ "emp", "HOME" }, { "hrm", "HOME" },
				{ "hrm", "CREATE SCHEDULE" }, { "hrm", "VIEW SCHEDULE" },
				{ "hrm", "MODIFY SCHEDULE" }, { "hrm", "VIEW SCHEDULE" },
				{ "hrm", "MODIFY SCHEDULE" }, { "hrm", "LOGOUT" },
				{ "hrm", "HOME" }, { "ta", "tm-schedule view" },
				{ "ta", "tm schedule modify" },
				{ "ta", "hrm scheudle modify" }, { "ta", "emp schedule view" },
				{ "ta", "User Mgmt" }, { "ta", "UNSUBSCRIBE" },
				{ "ta", "TRACK TRIP" }, { "ta", "SUBSCRIBE" },
				{ "ta", "SETTINGS" }, { "ta", "SCHEDULES" },
				{ "ta", "ROUTING" }, { "ta", "ROUTE MASTER" },
				{ "ta", "REPORTS" }, { "ta", "OTHERS" }, { "ta", "LOGOUT" },
				{ "ta", "HOME-vendor home" }, { "ta", "HOME-ta" },
				{ "ta", "ForEMP" }, { "ta", "FOR EMPLOYEES" },
				{ "ta", "CREATE SCHEDULE" }, { "ta", "CANCEL SCHEDULE" },
				{ "tm", "HOME" }, { "tm", "SETTINGS" },
				{ "tm", "ROUTE MASTER" }, { "tm", "ROUTING" },

				{ "tm", "FOR EMPLOYEES" }, { "tm", "REPORTS" },
				{ "tm", "ForEMP" }, { "tm", "User Mgmt" }, { "tm", "LOGOUT" },
				{ "tm", "HOME" }, { "v", "ROUTING" }, { "v", "LOGOUT" },
				{ "v", "TRACK TRIP" } };
		;
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		for (int i = 0; i < views.length; i++) {

			ViewManagementDto dto = templateToPageDtoForLoadRolesViewsAssociation_Default(views[i]);
			dtoList.add(dto);

		}

		new LoadSettingsDao().loadRolesViewsAssociation_Default(dtoList);

	}

	private PageDto templateToPageDtoForLoadPage_Default(String[] template) {
		PageDto dto = new PageDto();
		dto.setUrl(template[0]);
		dto.setUrlType(template[1]);
		return dto;
	}

	private PageDto templateToPageDtoForSetHomePage_Default(String[] template) {
		PageDto dto = new PageDto();
		dto.setUrl(template[0]);
		dto.setUserType(template[1]);
		return dto;
	}

	private ViewManagementDto templateToPageDtoForLoadViews_Default(
			String[] template) {
		ViewManagementDto dto = new ViewManagementDto();
		dto.setViewName(template[0]);
		dto.setViewKey(template[1]);
		try {
			dto.setViewShowOrder(Integer.parseInt(template[2]));
		} catch (Exception e) {
			dto.setViewShowOrder(0);
		}
		dto.setViewURL(template[3]);
		return dto;
	}

	private ViewManagementDto templateToPageDtoForLoadSubViews_Default(
			String[] template) {
		ViewManagementDto dto = new ViewManagementDto();
		dto.setSubViewName(template[0]);
		dto.setSubViewKey(template[1]);
		try {
			dto.setSubViewShowOrder(Integer.parseInt(template[2]));
		} catch (Exception e) {
			dto.setSubViewShowOrder(0);
		}
		dto.setSubViewURL(template[3]);
		dto.setViewName(template[4]);

		return dto;
	}

	private ViewManagementDto templateToPageDtoForLoadRolesViewsAssociation_Default(
			String[] template) {
		ViewManagementDto dto = new ViewManagementDto();
		dto.setRoleName(template[0]);
		dto.setViewName(template[1]);

		return dto;
	}

	public void loadAllPageViewSettingsDefaults() {
		// truncate all tables related to page and page_role autherisation
		if (new LoadSettingsDao().truncateAllPageRoleTables() == 6) {
			// 1. adding pages
			addPages();
			// 2. set Home pages
			setHomePage_Default();
			// 3. add main views
			loadViews_Default();
			// 4. add sub views
			loadSubViews_Default();
			// 5. assign roles and views
			loadRolesViewsAssociation_Default();
			// 6. assing page to role based on views
			new LoadSettingsDao().loadPageRole_Default();
			// 7. fill data in menu_url table for page access
			new LoadSettingsDao().loadMenu_Url_Default();
		}
	}

	public void printdbDataAs2DArray() throws FileNotFoundException {

		File f = new File("D:/ss/data.txt");
		FileReader fr = new FileReader(f);
		BufferedReader bf = new BufferedReader(fr);
		int i = 0;
		int j = 0;

		try {
			System.out.println("{ \n{\"");
			while (bf.ready()) {

				char c = (char) bf.read();

				i++;
				String ss = "";
				if (c == '\t') {
					ss = "\", \"";

				} else if (c == '\r') {
					// System.out.print("\");");
					ss = "\"}," + c;

					i = 0;
				} else if (c == '\n') {
					// System.out.print("(\"");
					ss = "" + c + "{\"";
				} else {
					ss = "" + c;
				}

				System.out.print(ss);

			}
			System.out.println("\"} \n};");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error : " + e);
		}

	}

}
