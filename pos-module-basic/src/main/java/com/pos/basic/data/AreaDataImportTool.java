/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.pos.basic.dao.AreaDao;
import com.pos.basic.domain.Area;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区初始数据的导入工具.
 *
 * 本工具不提供公共方法调用，运行main方法来执行数据导入，且只能在开发环境上执行.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
public class AreaDataImportTool {

    private List<Area> list = new ArrayList<>();

    private List<Area> missList = new ArrayList<>();

    private Map<String, Area> provinceMap = new HashMap<>();

    private Map<String, Integer> provinceOrderMap = new HashMap<>();

    private Map<String, Area> cityMap = new HashMap<>();

    private Map<String, Integer> cityOrderMap = new HashMap<>();

    private AreaDataImportTool() {
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("basic/applicationContext.xml");
        AreaDao areaDao = context.getBean(AreaDao.class);

        AreaDataImportTool importTool = new AreaDataImportTool();
        /**
         * excel打包到classes后会乱码，只保证本地开发环境下运行正常
         */
        importTool.loadData(System.getProperty("user.dir") + File.separator
                + "ywmj-module-basic" + File.separator + "ywmj-area-china.xls");

        System.out.println("成功载入的地区数量：" + importTool.list.size());
        System.out.println("关联失败的地区数量：" + importTool.missList.size());

        if (!importTool.missList.isEmpty()) {
            System.err.println("关联失败的地区列表 >>");
            for (Area area : importTool.missList) {
                System.err.println(area.toString());
            }
        } else if (!importTool.list.isEmpty()) {
            System.out.println("成功载入的地区列表 >>");
            List<Area> list = new ArrayList<>();
            for (Area area : importTool.list) {
                System.out.println(area.toString());
                list.add(area);
                if (list.size() % 100 == 0) {
                    areaDao.saveBatchArea(list);
                    list.clear();
                }
            }
            if (!list.isEmpty()) {
                areaDao.saveBatchArea(list);
            }
        }
        System.out.println("共写入DB " + areaDao.getTotal() + "条数据");
    }

    private Area createArea(long id, String name, byte level) {
        Area area = new Area();
        area.setId(id);
        area.setName(name);
        area.setLevel(level);
        area.setAvailable(true);
        area.setShortName(getShortName(name));
        return area;
    }

    private String getShortName(String name) {
        if (name.endsWith("省") || name.endsWith("市") || name.endsWith("盟")) {
                return name.substring(0, name.length() - 1);
        } else if (name.endsWith("县")) {
            if (name.length() > 2 && !name.endsWith("自治县")) {
                return name.substring(0, name.length() - 1);
            }
        } else if (name.endsWith("维吾尔自治区")) {
            return name.substring(0, name.length() - 6);
        } else if (name.equals("西藏自治区") || name.equals("内蒙古自治区")) {
            return name.substring(0, name.length() - 3);
        } else if (name.endsWith("特别行政区")
                || name.endsWith("回族自治区") || name.endsWith("壮族自治区")) {
            return name.substring(0, name.length() - 5);
        }

        return name;
    }

    private void setOrderNum(Area area, String key, Map<String, Integer> orderMap) {
        Integer orderNum = orderMap.get(key);
        if (orderNum != null) {
            orderNum++;
        } else {
            orderNum = 1;
        }
        area.setOrderNum(orderNum.byteValue());
        orderMap.put(key, orderNum);
    }

    private void loadData(String fileName) throws IOException {
        File file = new File(fileName);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

        int rowStart = hssfSheet.getFirstRowNum() + 1;
        int rowEnd = hssfSheet.getLastRowNum();
        for (int i = rowStart; i <= rowEnd; i++) {
            HSSFRow row = hssfSheet.getRow(i);
            if (row == null) {
                continue;
            }

            HSSFCell nameCell = row.getCell(1);
            HSSFCell levelCell = row.getCell(4);
            Area area = createArea(i, nameCell.getStringCellValue().trim(),
                    (byte) (levelCell.getNumericCellValue() + 1)); // Area等级以1开始计数

            switch (area.getLevel()) {
                case 1: // 省
                    area.setOrderNum((byte) (provinceMap.size() + 1));
                    provinceMap.put(area.getName(), area);
                    list.add(area);
                    break;
                case 2: // 市
                    HSSFCell ownerProvinceCell = row.getCell(2);
                    String provinceName = ownerProvinceCell.getStringCellValue().trim();
                    Area province = provinceMap.get(provinceName);
                    if (province != null) {
                        area.setParentId(province.getId());
                        setOrderNum(area, provinceName, provinceOrderMap);
                        list.add(area);
                    } else {
                        missList.add(area);
                    }
                    cityMap.put(area.getName(), area);
                    break;
                case 3: // 县/区
                    HSSFCell ownerCityCell = row.getCell(3);
                    String cityName = ownerCityCell.getStringCellValue().trim();
                    Area city = cityMap.get(cityName);
                    if (city != null) {
                        setOrderNum(area, cityName, cityOrderMap);
                        area.setParentId(city.getId());
                        list.add(area);
                    } else {
                        missList.add(area);
                    }
                    break;
                default:
                    throw new RuntimeException("错误的地区等级：" + area.toString());
            }
        }
    }

}