package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dao.RouteDao;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.GeoTagDto;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.dto.TripDetailsChildDto;

public class RouteService {
	public int insertRoute(ArrayList<RouteDto> routedto) {
		return (new RouteDao().insertRoute(routedto));
	}

	public int insertShuttleRoute(ArrayList<RouteDto> routedto) {
		int retVal = 0;
		try {
			if (routedto.get(0).getInOut().equalsIgnoreCase("BOTH")
					|| routedto.get(0).getInOut().equalsIgnoreCase("OUT")) {
				retVal = new RouteDao().insertShuttleRoute(routedto, "OUT");
			}
			if (routedto.get(0).getInOut().equalsIgnoreCase("BOTH")
					|| routedto.get(0).getInOut().equalsIgnoreCase("IN")) {
				Collections.reverse(routedto);
				retVal = new RouteDao().insertShuttleRoute(routedto, "IN");
			}
		} catch (Exception e) {
			System.out.println("Errror in shuttle add service" + e);
		}
		return retVal;

	}

	public ArrayList<RouteDto> getRoutes(String site) {
		return new RouteDao().getRoutes(site);
	}

	public ArrayList<RouteDto> getAllRoutes() {
		return new RouteDao().getAllRoutes();
	}

	public ArrayList<RouteDto> getAllRoutes(String site) {
		return new RouteDao().getAllRoutes(site);
	}

	public ArrayList<RouteDto> getAllShuttleRoutes(String site) {
		return new RouteDao().getAllShuttleRoutes(site);
	}

	public ArrayList<RouteDto> getInOutShuttleRoutes(String site, String inOut) {
		return new RouteDao().getInOutShuttleRoutes(site, inOut);
	}

	public ArrayList<APLDto> getAllAPLNotInRoute(int routeId) {
		return new APLService().getAllAPLNotInRoute(routeId);
	}

	public ArrayList<RouteDto> getAllAPLInRoute(int routeId) {
		return new RouteDao().getRouteDetails(routeId);
	}

	public ArrayList<APLDto> getAllShuttleAPLNotInRoute(int routeId) {
		return new APLService().getAllShuttleAPLNotInRoute(routeId);
	}

	public ArrayList<RouteDto> getAllShuttleAPLInRoute(int routeId) {
		return new RouteDao().getShuttleRouteDetails(routeId);
	}

	public int modifyRoute(ArrayList<RouteDto> routeLandmarks, int routeId) {
		// TODO Auto-generated method stub
		return (new RouteDao().modifyRoute(routeLandmarks, routeId));
	}

	public int modifyShuttleRoute(ArrayList<RouteDto> routeLandmarks,
			int routeId) {
		// TODO Auto-generated method stub
		return (new RouteDao().modifyShuttleRoute(routeLandmarks, routeId));
	}

	public RouteDto getMasterRouteDetails(int routeId) {
		// TODO Auto-generated method stub
		return (new RouteDao().getMasterRouteDetails(routeId));
	}

	public RouteDto getShuttleMasterRouteDetails(int routeId) {
		// TODO Auto-generated method stub
		return (new RouteDao().getShuttleMasterRouteDetails(routeId));
	}

	public ArrayList<RouteDto> getRoutetypes() {
		return (new RouteDao().getRoutetypes());
	}

	public int updateMasterRoute(RouteDto routeDto) {
		return (new RouteDao().updateMasterRoute(routeDto));
	}

	public int updateShuttleMasterRoute(RouteDto routeDto) {
		return (new RouteDao().updateShuttleMasterRoute(routeDto));
	}

	public int assignShuttleVehicle(ArrayList<RouteDto> list, int totalSeats) {
		int status = new RouteDao().assignShuttleVehicle(list);
		new ShuttleServcie().updateBookingWaitingList(list, totalSeats);
		return status;
	}

	public ArrayList<RouteDto> getAllAPLInTrip(int tripId) {
		return new RouteDao().getTripRouteDetails(tripId);
	}

	public ArrayList<TripDetailsChildDto> getEmployeeGetInPosition(int tripId) {
		return new TripDetailsDao().getEmployeeGetInPosition(tripId);
	}

	public ArrayList<TripDetailsChildDto> getEmployeePosition(int tripId) {
		return new TripDetailsDao().getEmployeePosition(tripId);
	}

	public int orderTheRoute(String siteId) {
		int retVal = 0;
		Object connectionObject = null;
		DistanceListDao distanceListDao = null;
		try {
			Map<String, Float> LandmarkDist = new HashMap<String, Float>();
			RouteDao dao = new RouteDao();
			ArrayList<RouteDto> currentRoutes = dao.getAllRoutes(siteId);
			RouteDao routeDao = new RouteDao();
			distanceListDao = new DistanceListDao();
			connectionObject = distanceListDao.getConnection();
			float distt = (float) 0.0;
			for (RouteDto routeDto : currentRoutes) {
				System.out.println(routeDto.getRouteId() + "  " + new Date());
				ArrayList<RouteDto> landmarksOfROute = dao
						.getRouteDetailsInRevers(routeDto.getRouteId(),
								connectionObject);
				LandmarkDist = new HashMap<String, Float>();
				float intDist = (float) -1.0;
				int lastLandmarkPos = 0;
				for (int i = 0; i < landmarksOfROute.size(); i++) {

					distt = distanceListDao.getDistance(landmarksOfROute.get(0)
							.getLandmarkId(), landmarksOfROute.get(i)
							.getLandmarkId(), connectionObject);
					if (intDist < distt) {
						System.out.println(landmarksOfROute.get(i)
								.getLandmarkId() + "        " + distt);
						lastLandmarkPos = i;
						intDist = distt;
					}
				}
				System.out.println(landmarksOfROute.get(lastLandmarkPos)
						.getLandmarkId());
				System.out
						.println("<<<<<<<lastLandmarkPos point take"
								+ landmarksOfROute.get(lastLandmarkPos)
										.getLandmarkId());
				for (int i = 0; i < landmarksOfROute.size(); i++) {
					if (i == lastLandmarkPos) {
						LandmarkDist.put(landmarksOfROute.get(lastLandmarkPos)
								.getLandmarkId(), (float) 0.0);
						System.out.println("Lats " + 0);
					} else {
						distt = distanceListDao.getDistance(landmarksOfROute
								.get(lastLandmarkPos).getLandmarkId(),
								landmarksOfROute.get(i).getLandmarkId(),
								connectionObject);
						LandmarkDist.put(landmarksOfROute.get(i)
								.getLandmarkId(), distt);
						System.out.println(distt);
					}
				}

				LandmarkDist = sortTheMap(LandmarkDist);
				retVal += routeDao.orderTheRoute(LandmarkDist.keySet()
						.toArray(), routeDto.getRouteId());
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			distanceListDao.closeConnection(connectionObject);
		}
		System.out.println(retVal);
		return retVal;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortTheMap(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	// only for keonics
	public ArrayList<RouteDto> getAllRoutesWithlog(String site, String logType) {
		return new RouteDao().getAllRoutesWithlog(site, logType);
	}

	// only for keonics
	public int createRouteWithoutAPL(String Routename, String logType,
			String[] landmarks, String landmarkName[]) {
		return new RouteDao().createRouteWithoutAPL(Routename, logType,
				landmarks, landmarkName);
	}

	// only for keonics
	public ArrayList<RouteDto> getRouteDetailsWithoutAPL(int routeId) {
		return new RouteDao().getRouteDetailsWithoutAPL(routeId);
	}

	// only for keonics
	public ArrayList<ArrayList<RouteDto>> getAllRouteDetailsWithoutAPL(
			String[] routeids) {
		return new RouteDao().getAllRouteDetailsWithoutAPL(routeids);
	}
	

	// only for Keonics
	public ArrayList<RouteDto> getAllRoutesWithNoSites(String logType) {
		return new RouteDao().getAllRoutesWithNoSites(logType);
	}

	// only for Keonics
	public int createAutoRoute(String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids) {
		// TODO Auto-generated method stub
		if (vehiceType.equalsIgnoreCase("4")) {
			vehiceType = "Indica";
		} else if (vehiceType.equalsIgnoreCase("7")) {
			vehiceType = "Sumo";
		} else if (vehiceType.equalsIgnoreCase("13")) {
			vehiceType = "Tempo";
		} else if (vehiceType.equalsIgnoreCase("18")) {
			vehiceType = "Mazda";
		} else if (vehiceType.equalsIgnoreCase("30")) {
			vehiceType = "Mini Bus";
		} else if (vehiceType.equalsIgnoreCase("50")) {
			vehiceType = "Bus";
		} else if (vehiceType.equalsIgnoreCase("80")) {
			vehiceType = "Bus 1";
		}
		System.out.println(empids.length+"idssss");
		return new RouteDao().createAutoRoute(routename, routeType, points,
				landmarkName, vehiceType, empids);
	}

	// keo
	public ArrayList<RouteDto> getAutogeneratedRoutelist() {
		return new RouteDao().getAutogeneratedRoutelist();
	}

	// keo
	public RouteDto getAutogeneratedRoute(String routeid) {
		return new RouteDao().getAutogeneratedRoute(routeid);
	}
	
	//keo
	public ArrayList<GeoTagDto> getEmployeeFromRoute(String routeid){
		return new RouteDao().getEmployeeFromRoute(routeid);
	}

	public int UpdateAutoRoute(String routeid,String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids) {
		// TODO Auto-generated method stub
		if (vehiceType.equalsIgnoreCase("4")) {
			vehiceType = "Indica";
		} else if (vehiceType.equalsIgnoreCase("7")) {
			vehiceType = "Sumo";
		} else if (vehiceType.equalsIgnoreCase("13")) {
			vehiceType = "Tempo";
		} else if (vehiceType.equalsIgnoreCase("18")) {
			vehiceType = "Mazda";
		} else if (vehiceType.equalsIgnoreCase("30")) {
			vehiceType = "Mini Bus";
		} else if (vehiceType.equalsIgnoreCase("50")) {
			vehiceType = "Bus";
		} else if (vehiceType.equalsIgnoreCase("80")) {
			vehiceType = "Bus1";
		}
		return new RouteDao().updateAutoRoute(routeid,routename, routeType, points,
				landmarkName, vehiceType, empids);
	}

	public int createAutoRoutewithLog(String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time) {
		// TODO Auto-generated method stub
		if (vehiceType.equalsIgnoreCase("4")) {
			vehiceType = "Indica";
		} else if (vehiceType.equalsIgnoreCase("7")) {
			vehiceType = "Sumo";
		} else if (vehiceType.equalsIgnoreCase("13")) {
			vehiceType = "Tempo";
		} else if (vehiceType.equalsIgnoreCase("18")) {
			vehiceType = "Mazda";
		} else if (vehiceType.equalsIgnoreCase("30")) {
			vehiceType = "Mini Bus";
		} else if (vehiceType.equalsIgnoreCase("50")) {
			vehiceType = "Bus";
		} else if (vehiceType.equalsIgnoreCase("80")) {
			vehiceType = "Bus 1";
		}
		System.out.println(empids.length+"idssss");
		return new RouteDao().createAutoRoutewithLog(routename, routeType, points,
				landmarkName, vehiceType, empids,time);
	}
	
	// tokai
	public ArrayList<RouteDto> getAutogeneratedRoutelistWithLog() {
		return new RouteDao().getAutogeneratedRoutelistWithLog();
	}
	// autoRouting
	public RouteDto getAutogeneratedRouteWithLog(String routeid) {
		return new RouteDao().getAutogeneratedRouteWithLog(routeid);
	}
	//AutRoutes
	public int UpdateAutoRouteWithLog(String routeid,String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids,String time) {
		// TODO Auto-generated method stub
		if (vehiceType.equalsIgnoreCase("4")) {
			vehiceType = "Indica";
		} else if (vehiceType.equalsIgnoreCase("7")) {
			vehiceType = "Sumo";
		} else if (vehiceType.equalsIgnoreCase("13")) {
			vehiceType = "Tempo";
		} else if (vehiceType.equalsIgnoreCase("18")) {
			vehiceType = "Mazda";
		} else if (vehiceType.equalsIgnoreCase("30")) {
			vehiceType = "Mini Bus";
		} else if (vehiceType.equalsIgnoreCase("50")) {
			vehiceType = "Bus";
		} else if (vehiceType.equalsIgnoreCase("80")) {
			vehiceType = "Bus1";
		}
		return new RouteDao().updateAutoRouteWithLog(routeid,routename, routeType, points,
				landmarkName, vehiceType, empids,time);
	}

	public int createShuttleRoute(String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time,String[] compDist,String[] noodleDist) {
		// TODO Auto-generated method stub
		return new RouteDao().createShuttleRoute(routename, routeType, points,
				landmarkName, vehiceType, empids,time,compDist,noodleDist);
	}
	
	// autoroute
		public ArrayList<RouteDto> getShuttleRoutes() {
			return new RouteDao().getShuttleRoutes();
		}
	//autoroute
		public ArrayList<GeoTagDto> getEmployeeFromShuttleRoute(String routeid){
			return new RouteDao().getEmployeeFromShuttleRoute(routeid);
		}

		public int createShuttleRoute1(String routename, String routeType,
				String[] points, String[] landmarkName, String vehiceType,
				String[] empids, String time, String[] compDist,
				String[] noodleDist, String filter,String travelTime,String site) {
			// TODO Auto-generated method stub
			if (vehiceType.equalsIgnoreCase("4")) {
				vehiceType = "Indica";
			} else if (vehiceType.equalsIgnoreCase("7")) {
				vehiceType = "Sumo";
			} else if (vehiceType.equalsIgnoreCase("12")) {
				vehiceType = "Tempo";
			} else if (vehiceType.equalsIgnoreCase("18")) {
				vehiceType = "Mazda";
			} else if (vehiceType.equalsIgnoreCase("24")) {
				vehiceType = "Mini Bus";
			} else if (vehiceType.equalsIgnoreCase("40")) {
				vehiceType = "Bus";
			} else if (vehiceType.equalsIgnoreCase("80")) {
				vehiceType = "Bus 1";
			}
			return new RouteDao().createShuttleRoute1(routename, routeType, points,
					landmarkName, vehiceType, empids,time,compDist,noodleDist,filter,travelTime,site);
		}
		// autoRouting
		public RouteDto getAutogeneratedRouteWithFilter(String routeid) {
			return new RouteDao().getAutogeneratedRouteWithFilter(routeid);
		}

		public int disableAutoRoute(String routeid) {
			return new RouteDao().disableAutoRoute(routeid);
		}
		
		public ArrayList<GeoTagDto> getEmployeeFromFilteredRoute(String routeid){
			return new RouteDao().getEmployeeFromFilteredRoute(routeid);
		}

		public int UpdateAutoRouteWithFilter(String siteid,String routeid, String routename,
				String routeType, String[] points, String[] landmarkName,
				String vehiceType, String[] empids, String time,
				String[] compDist, String[] noodleDist, String filter,String travelTime) {
			// TODO Auto-generated method stub
			if (vehiceType.equalsIgnoreCase("4")) {
				vehiceType = "Indica";
			} else if (vehiceType.equalsIgnoreCase("7")) {
				vehiceType = "Sumo";
			} else if (vehiceType.equalsIgnoreCase("12")) {
				vehiceType = "Tempo";
			} else if (vehiceType.equalsIgnoreCase("18")) {
				vehiceType = "Mazda";
			} else if (vehiceType.equalsIgnoreCase("24")) {
				vehiceType = "Mini Bus";
			} else if (vehiceType.equalsIgnoreCase("40")) {
				vehiceType = "Bus";
			} else if (vehiceType.equalsIgnoreCase("80")) {
				vehiceType = "Bus 1";
			}
			return new RouteDao().UpdateAutoRouteWithFilter(siteid,routeid,routename, routeType, points,
					landmarkName, vehiceType, empids,time,compDist,noodleDist,filter,travelTime);
		}
		
}