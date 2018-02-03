package com.redhat.training.msa.hello;

import org.springframework.stereotype.Component;

@Component
public class BonjourServiceImpl implements BonjourService {

   @Override
   public String bonjour() {
     return "Bonjour de ";
   }
}
