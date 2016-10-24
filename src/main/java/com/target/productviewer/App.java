package com.target.productviewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	private static final Logger log = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        System.out.println( "Starting ProductViewer..." );
        SpringApplication.run(App.class, args);
    }
    
}
