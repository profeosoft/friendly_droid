package com.profeosoft.service;

import com.profeosoft.repository.DictsRepository;

import java.util.List;

/**
 * Created by profeosoft@gmail.com on 2016-04-23.
 */
public class DictsServcies {

    private DictsRepository dictRepo;

    public DictsServcies(){
        dictRepo = new DictsRepository();
    }

    public List<String> getItemsByDictName(String dictName){
        try{
            return dictRepo.getItemsByDictName(dictName);
        } catch(Exception ex) {
            //LOG.e();
            throw ex;
        }
    }
}
