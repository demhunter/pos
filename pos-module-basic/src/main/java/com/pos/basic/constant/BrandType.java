/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.basic.dto.brand.BrandTypeDto;
import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/8/26
 */
public enum BrandType implements CommonByteEnum {

    硬装主材((byte) 1, new SubType[]{
            HardMainMaterial.石材, HardMainMaterial.瓷砖, HardMainMaterial.地板, HardMainMaterial.雕花,
            HardMainMaterial.线条, HardMainMaterial.硅藻泥, HardMainMaterial.护墙板, HardMainMaterial.洁具,
            HardMainMaterial.卫浴, HardMainMaterial.橱柜, HardMainMaterial.定制柜, HardMainMaterial.门窗,
            HardMainMaterial.吊顶, HardMainMaterial.锁具, HardMainMaterial.五金, HardMainMaterial.玻璃,
            HardMainMaterial.楼梯, HardMainMaterial.铁艺, HardMainMaterial.马赛克, HardMainMaterial.其它
    }),

    墙纸布艺((byte) 2, new SubType[]{
            WallpaperFabric.墙纸, WallpaperFabric.布艺, WallpaperFabric.软包, WallpaperFabric.硬包,
            WallpaperFabric.窗帘, WallpaperFabric.床品, WallpaperFabric.地毯, WallpaperFabric.枕垫,
            WallpaperFabric.其它
    }),

    家具陈列((byte) 3, new SubType[]{
            FurnitureDisplay.板式, FurnitureDisplay.实木, FurnitureDisplay.原木, FurnitureDisplay.藤艺,
            FurnitureDisplay.布艺, FurnitureDisplay.石艺, FurnitureDisplay.金属, FurnitureDisplay.玻璃,
            FurnitureDisplay.其它
    }),

    灯具饰品((byte) 4, new SubType[]{
            LightingDecoration.工艺灯, LightingDecoration.常规灯, LightingDecoration.园艺灯, LightingDecoration.挂饰,
            LightingDecoration.摆件, LightingDecoration.花艺, LightingDecoration.其它
    }),

    家用电器((byte) 5, new SubType[]{
            Appliance.影视, Appliance.娱乐, Appliance.卫洗, Appliance.厨电, Appliance.其它
    }),

    设备设施((byte) 6, new SubType[]{
            Facilities.空调, Facilities.新风, Facilities.地暖, Facilities.智能, Facilities.安防,
            Facilities.净水, Facilities.影院, Facilities.音响, Facilities.园艺, Facilities.其它
    });

    private final byte code;

    private final SubType[] childList;

    BrandType(final byte code, final SubType[] childList) {
        if (childList == null || childList.length == 0) {
            throw new NullPointerException("`childList` not empty!");
        }
        this.code = code;
        this.childList = childList;
    }

    public byte getCode() {
        return code;
    }

    public SubType[] getChildList() {
        return childList;
    }

    public boolean compare(byte code) {
        return this.code == code;
    }

    public static BrandType getEnum(byte code) {
        for (BrandType type : BrandType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取指定品牌类型的名称.
     *
     * @param typeId 品牌大类ID
     * @return 类型名称
     */
    public static String getTypeName(short typeId) {
        BrandType type = BrandType.getEnum((byte) typeId);
        return type != null ? type.name() : null;
    }

    /**
     * 获取指定品牌子类型的名称.
     *
     * @param typeId    品牌大类ID
     * @param subTypeId 品牌子类ID
     * @return 类型名称
     */
    public static String getSubTypeName(short typeId, short subTypeId) {
        BrandType parent = BrandType.getEnum((byte) typeId);
        if (parent != null) {
            for (BrandType.SubType child : parent.getChildList()) {
                if (child.compare(subTypeId)) {
                    return child.getName();
                }
            }
        }
        return null;
    }

    /**
     * 获取品牌类型列表.
     *
     * @param getAll true表示获取所有层级的品牌, false表示只获取第一层级的品牌
     */
    public static List<BrandTypeDto> getTypes(boolean getAll) {
        List<BrandTypeDto> list = new ArrayList<>(BrandType.values().length);
        if (getAll) {
            for (BrandType type : BrandType.values()) {
                BrandTypeDto dto = new BrandTypeDto(type.getCode(), type.name());
                List<BrandTypeDto> subList = new ArrayList<>(type.getChildList().length);
                for (BrandType.SubType subType : type.getChildList()) {
                    subList.add(new BrandTypeDto((short) subType.getValue(), subType.getName()));
                }
                dto.setSubTypes(subList);
                list.add(dto);
            }
        } else {
            for (BrandType type : BrandType.values()) {
                list.add(new BrandTypeDto(type.getCode(), type.name()));
            }
        }
        return list;
    }

    /**
     * 获取指定品牌类型的子类型列表.
     *
     * @param typeId 品牌大类ID
     */
    public static List<BrandTypeDto> getSubTypes(short typeId) {
        BrandType type = BrandType.getEnum((byte) typeId);
        if (type != null) {
            List<BrandTypeDto> subList = new ArrayList<>(type.getChildList().length);
            for (BrandType.SubType subType : type.getChildList()) {
                subList.add(new BrandTypeDto((short) subType.getValue(), subType.getName()));
            }
            return subList;
        } else {
            return null;
        }
    }

    /**
     * 品牌子类型接口.
     */
    public interface SubType {

        String getName();

        BrandType getParent();

        boolean compare(int code);

        int getValue();
    }

    /**
     * 硬装主材子类型定义.
     */
    public enum HardMainMaterial implements SubType, CommonIntEnum {

        石材(1101, BrandType.硬装主材),
        瓷砖(1102, BrandType.硬装主材),
        地板(1103, BrandType.硬装主材),
        雕花(1104, BrandType.硬装主材),
        线条(1105, BrandType.硬装主材),
        硅藻泥(1106, BrandType.硬装主材),
        护墙板(1107, BrandType.硬装主材),
        洁具(1108, BrandType.硬装主材),
        卫浴(1109, BrandType.硬装主材),
        橱柜(1110, BrandType.硬装主材),
        定制柜(1111, BrandType.硬装主材),
        门窗(1112, BrandType.硬装主材),
        吊顶(1113, BrandType.硬装主材),
        锁具(1114, BrandType.硬装主材),
        五金(1115, BrandType.硬装主材),
        玻璃(1116, BrandType.硬装主材),
        楼梯(1117, BrandType.硬装主材),
        铁艺(1118, BrandType.硬装主材),
        马赛克(1119, BrandType.硬装主材),
        其它(1199, BrandType.硬装主材);

        private final int code;

        private final BrandType parent;

        HardMainMaterial(final int code, final BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }

    }

    /**
     * 墙纸布艺子类型定义
     */
    public enum WallpaperFabric implements SubType, CommonIntEnum {

        墙纸(1201, BrandType.墙纸布艺), //（原来在材）
        布艺(1202, BrandType.墙纸布艺), //（原来在饰品）
        软包(1203, BrandType.墙纸布艺),
        硬包(1204, BrandType.墙纸布艺),
        窗帘(1205, BrandType.墙纸布艺), //（原来在饰品）
        床品(1206, BrandType.墙纸布艺),
        地毯(1207, BrandType.墙纸布艺), //（原来在饰品）
        枕垫(1208, BrandType.墙纸布艺),
        其它(1299, BrandType.墙纸布艺);

        private final int code;

        private final BrandType parent;

        WallpaperFabric(int code, BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }
    }

    /**
     * 家具陈列子类型定义.
     */
    public enum FurnitureDisplay implements SubType, CommonIntEnum {

        板式(1301, BrandType.家具陈列),
        实木(1302, BrandType.家具陈列),
        原木(1303, BrandType.家具陈列),
        藤艺(1304, BrandType.家具陈列),
        布艺(1305, BrandType.家具陈列),
        石艺(1306, BrandType.家具陈列),
        金属(1307, BrandType.家具陈列),
        玻璃(1308, BrandType.家具陈列),
        其它(1399, BrandType.家具陈列);

        private final int code;

        private final BrandType parent;

        FurnitureDisplay(final int code, final BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }

    }

    /**
     * 灯具饰品子类型定义
     */
    public enum LightingDecoration implements SubType, CommonIntEnum {

        工艺灯(1401, BrandType.灯具饰品),
        常规灯(1402, BrandType.灯具饰品),
        园艺灯(1403, BrandType.灯具饰品), // （原来在灯具）
        挂饰(1404, BrandType.灯具饰品),
        摆件(1405, BrandType.灯具饰品),
        花艺(1406, BrandType.灯具饰品),
        其它(1499, BrandType.灯具饰品);

        private final int code;

        private final BrandType parent;

        LightingDecoration(int code, BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }
    }

    /**
     * 家用电器子类型定义
     */
    public enum Appliance implements SubType, CommonIntEnum {
        影视(1501, BrandType.家用电器),
        娱乐(1502, BrandType.家用电器),
        卫洗(1503, BrandType.家用电器),
        厨电(1504, BrandType.家用电器),
        其它(1599, BrandType.家用电器);

        private final int code;

        private final BrandType parent;

        Appliance(int code, BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }
    }

    /**
     * 设备设施子类型定义
     */
    public enum Facilities implements SubType, CommonIntEnum {

        空调(1601, BrandType.设备设施),
        新风(1602, BrandType.设备设施),
        地暖(1603, BrandType.设备设施),
        智能(1604, BrandType.设备设施),
        安防(1605, BrandType.设备设施),
        净水(1606, BrandType.设备设施),
        影院(1607, BrandType.设备设施),
        音响(1608, BrandType.设备设施),
        园艺(1609, BrandType.设备设施),
        其它(1699, BrandType.设备设施);

        private final int code;

        private final BrandType parent;

        Facilities(int code, BrandType parent) {
            this.code = code;
            this.parent = parent;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public BrandType getParent() {
            return parent;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getValue() {
            return code;
        }
    }

}
