package pers.traveler.engine;

import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;
import pers.traveler.constant.Package;
import pers.traveler.entity.Config;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by quqing on 16/5/18.
 * 引擎工厂
 */
public class EngineFactory {
    public static Engine build(String engineType, AppiumDriver<WebElement> driver, Config config) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(Package.ENGINE.replaceAll("#type#", engineType));
        return (Engine) clazz.getConstructor(AppiumDriver.class, Config.class).newInstance(driver, config);
    }
}