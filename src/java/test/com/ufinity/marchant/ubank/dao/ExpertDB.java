package com.ufinity.marchant.ubank.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author WenQiang Wu
 * 
 * @time Jun 24, 2009 11:05:13 AM
 */
public class ExpertDB {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        EntityManagerFactory emFactory = Persistence
                .createEntityManagerFactory("ubank");
        // EntityManager em = emFactory.createEntityManager();
    }

}
