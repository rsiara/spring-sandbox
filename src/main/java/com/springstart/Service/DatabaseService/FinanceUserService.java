package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.FinanceUserDao;
import com.springstart.Model.Entity.DatabaseEntity.FinanceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FinanceUserService {

    FinanceUserDao financeUserDao;

    public FinanceUser createFinanceUser(FinanceUser financeUser) {
        return financeUserDao.createFinanceUser(financeUser);
    }

    public FinanceUserDao getFinanceUserDao() {
        return financeUserDao;
    }

    @Autowired
    public void setFinanceUserDao(FinanceUserDao financeUserDao) {
        this.financeUserDao = financeUserDao;
    }

    public List<FinanceUser> getAllFinanceUsers() {
        return financeUserDao.findAll();
    }

    public FinanceUser getById(int user_id) {
        return financeUserDao.getById(user_id);
    }
}
