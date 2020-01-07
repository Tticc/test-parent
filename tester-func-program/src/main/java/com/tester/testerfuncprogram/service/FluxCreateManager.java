package com.tester.testerfuncprogram.service;

import reactor.core.publisher.Flux;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FluxCreateManager {
    public static void main(String[] args) throws InterruptedException {
        test_fluxCreate_create_already();

    }

    public static void test_fluxCreate_create_already(){
        List<ActionListener> listeners = new ArrayList<>();
        listeners.add((e) -> {
                System.out.println(e);
        });
        listeners.add((e) -> {
            System.out.println(e.getActionCommand());
        });

        Flux.create(sink -> {
            for (ActionListener item : listeners) {
                sink.next(item);
            }
        }).subscribe();

        listeners.forEach(action -> action.actionPerformed(
                new ActionEvent(FluxCreateManager.class, 1, "open happens"))
        );
    }
    public static void test_fluxCreate_create(){
        List<ActionListener> listeners = new ArrayList<>();

        Flux.create(sink -> {
            listeners.add(evt -> sink.next(evt));
            listeners.add(evt -> {
                System.out.println("event command is: " + evt.getActionCommand());
                sink.next(evt);
            });
        }).subscribe(evt -> eventAction((ActionEvent)evt));

        listeners.forEach(action -> action.actionPerformed(
                new ActionEvent(FluxCreateManager.class, 1, "open happens"))
        );
    }
    public static void eventAction(ActionEvent evt){
        System.out.println(evt.paramString());
    }
}
