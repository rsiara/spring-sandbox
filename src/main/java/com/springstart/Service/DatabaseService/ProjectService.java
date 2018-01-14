package com.springstart.Service.DatabaseService;


import com.springstart.Model.Entity.DatabaseEntity.ParkingPlace;
import com.springstart.Model.Entity.DatabaseEntity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProjectService {

    private ProjectDao projectDao;

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    @Autowired
    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    /* Methods */

    public Project getById(Long id){
        return projectDao.getById(id);
    }

    public void save(Project project){
        projectDao.save(project);
    }

    /**/



}
