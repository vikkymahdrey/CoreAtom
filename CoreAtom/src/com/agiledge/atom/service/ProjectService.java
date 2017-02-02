package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.ProjectDao;
import com.agiledge.atom.dto.ProjectDto;


public class ProjectService {

	public int mapTimeAndProject(String project, String timeId,String doneBy) {
		return new ProjectDao().mapTimeAndProject(project,timeId,doneBy);
		
	}
	
	public ArrayList<ProjectDto> getProjectsInShitTime(int timeID)
	{
		return new ProjectDao().getProjectsInShitTime(timeID);
	}

	public int removeMapTimeAndProject(String project, String timeId,String DoneBy) {
			return new ProjectDao().removeMapTimeAndProject( project,  timeId,DoneBy); 
	}
	public ArrayList<ProjectDto> getProjectUnit() {
		return new ProjectDao().getProjectUnit();
	}
	
}
