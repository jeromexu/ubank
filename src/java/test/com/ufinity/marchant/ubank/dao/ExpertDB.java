package com.ufinity.marchant.ubank.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author WenQiang Wu
 * @version Aug 19, 2010
 */
public class ExpertDB {

    /**
     * 
     * @param args
     * @author skyqiang
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        EntityManagerFactory emFactory = Persistence
                .createEntityManagerFactory("ubank");
        // EntityManager em = emFactory.createEntityManager();
    }

}
