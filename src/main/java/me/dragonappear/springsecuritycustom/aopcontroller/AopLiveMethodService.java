package me.dragonappear.springsecuritycustom.aopcontroller;

import org.springframework.stereotype.Service;

@Service
public class AopLiveMethodService {
    public void liveMethodSecured(){
        System.out.println("liveMethodSecured");
    }
}
