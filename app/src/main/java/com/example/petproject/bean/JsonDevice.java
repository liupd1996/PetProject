package com.example.petproject.bean;

public class JsonDevice {

    /**
     * RouteType : 0
     * Data : {"TerminalID":"10069096400","Longitude":118.89753280559943,"Latitude":32.057690778177204,"LocTime":"2024-01-04T21:55:23","RecvTime":"2024-01-04T13:55:24.2613409+00:00","Speed":0,"Direction":0,"DirectionDesc":"北","Mileage":0,"ExtendParam":"1=0&8=17&9=0&14=16121&31=3780&52=0&53=9481&80=64&83=1&84=1&90=0&91=0&92=0&93=-22_52_-1012&96=0_0","ExtendParamDesc":"ACC关  信号强度:17  卫星数:0  停车:11日4小时41分  总电压:3.78V  当日里程:9.48km  电量:64%  定位方式:基站  定位方式:WIFI  加速度传感器:[X轴]-22 [Y轴]52 [Z轴]-1012  温度:0℃,0℃","IsValid":true,"IsAlarm":false,"IsOnline":true,"IsAccOn":false,"State":"000C000000000000","StateDesc":"车辆电路正常/ACC关/未定位/北纬/东经","LastLocTime":"2024-01-04T21:55:23","IsInWhiteList":true}
     */
    private int RouteType;
    private DataBean Data;

    public int getRouteType() {
        return RouteType;
    }

    public void setRouteType(int RouteType) {
        this.RouteType = RouteType;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * TerminalID : 10069096400
         * Longitude : 118.89753280559943
         * Latitude : 32.057690778177204
         * LocTime : 2024-01-04T21:55:23
         * RecvTime : 2024-01-04T13:55:24.2613409+00:00
         * Speed : 0
         * Direction : 0
         * DirectionDesc : 北
         * Mileage : 0
         * ExtendParam : 1=0&8=17&9=0&14=16121&31=3780&52=0&53=9481&80=64&83=1&84=1&90=0&91=0&92=0&93=-22_52_-1012&96=0_0
         * ExtendParamDesc : ACC关  信号强度:17  卫星数:0  停车:11日4小时41分  总电压:3.78V  当日里程:9.48km  电量:64%  定位方式:基站  定位方式:WIFI  加速度传感器:[X轴]-22 [Y轴]52 [Z轴]-1012  温度:0℃,0℃
         * IsValid : true
         * IsAlarm : false
         * IsOnline : true
         * IsAccOn : false
         * State : 000C000000000000
         * StateDesc : 车辆电路正常/ACC关/未定位/北纬/东经
         * LastLocTime : 2024-01-04T21:55:23
         * IsInWhiteList : true
         */

        public String TerminalID;
        public double Longitude;
        public double Latitude;
        private String LocTime;
        private String RecvTime;
        private int Speed;
        private int Direction;
        private String DirectionDesc;
        private int Mileage;
        private String ExtendParam;
        private String ExtendParamDesc;
        private boolean IsValid;
        private boolean IsAlarm;
        private boolean IsOnline;
        private boolean IsAccOn;
        private String State;
        private String StateDesc;
        private String LastLocTime;
        private boolean IsInWhiteList;

        public String getTerminalID() {
            return TerminalID;
        }

        public void setTerminalID(String TerminalID) {
            this.TerminalID = TerminalID;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double Longitude) {
            this.Longitude = Longitude;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double Latitude) {
            this.Latitude = Latitude;
        }

        public String getLocTime() {
            return LocTime;
        }

        public void setLocTime(String LocTime) {
            this.LocTime = LocTime;
        }

        public String getRecvTime() {
            return RecvTime;
        }

        public void setRecvTime(String RecvTime) {
            this.RecvTime = RecvTime;
        }

        public int getSpeed() {
            return Speed;
        }

        public void setSpeed(int Speed) {
            this.Speed = Speed;
        }

        public int getDirection() {
            return Direction;
        }

        public void setDirection(int Direction) {
            this.Direction = Direction;
        }

        public String getDirectionDesc() {
            return DirectionDesc;
        }

        public void setDirectionDesc(String DirectionDesc) {
            this.DirectionDesc = DirectionDesc;
        }

        public int getMileage() {
            return Mileage;
        }

        public void setMileage(int Mileage) {
            this.Mileage = Mileage;
        }

        public String getExtendParam() {
            return ExtendParam;
        }

        public void setExtendParam(String ExtendParam) {
            this.ExtendParam = ExtendParam;
        }

        public String getExtendParamDesc() {
            return ExtendParamDesc;
        }

        public void setExtendParamDesc(String ExtendParamDesc) {
            this.ExtendParamDesc = ExtendParamDesc;
        }

        public boolean isIsValid() {
            return IsValid;
        }

        public void setIsValid(boolean IsValid) {
            this.IsValid = IsValid;
        }

        public boolean isIsAlarm() {
            return IsAlarm;
        }

        public void setIsAlarm(boolean IsAlarm) {
            this.IsAlarm = IsAlarm;
        }

        public boolean isIsOnline() {
            return IsOnline;
        }

        public void setIsOnline(boolean IsOnline) {
            this.IsOnline = IsOnline;
        }

        public boolean isIsAccOn() {
            return IsAccOn;
        }

        public void setIsAccOn(boolean IsAccOn) {
            this.IsAccOn = IsAccOn;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getStateDesc() {
            return StateDesc;
        }

        public void setStateDesc(String StateDesc) {
            this.StateDesc = StateDesc;
        }

        public String getLastLocTime() {
            return LastLocTime;
        }

        public void setLastLocTime(String LastLocTime) {
            this.LastLocTime = LastLocTime;
        }

        public boolean isIsInWhiteList() {
            return IsInWhiteList;
        }

        public void setIsInWhiteList(boolean IsInWhiteList) {
            this.IsInWhiteList = IsInWhiteList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "TerminalID='" + TerminalID + '\'' +
                    ", Longitude=" + Longitude +
                    ", Latitude=" + Latitude +
                    ", LocTime='" + LocTime + '\'' +
                    ", RecvTime='" + RecvTime + '\'' +
                    ", Speed=" + Speed +
                    ", Direction=" + Direction +
                    ", DirectionDesc='" + DirectionDesc + '\'' +
                    ", Mileage=" + Mileage +
                    ", ExtendParam='" + ExtendParam + '\'' +
                    ", ExtendParamDesc='" + ExtendParamDesc + '\'' +
                    ", IsValid=" + IsValid +
                    ", IsAlarm=" + IsAlarm +
                    ", IsOnline=" + IsOnline +
                    ", IsAccOn=" + IsAccOn +
                    ", State='" + State + '\'' +
                    ", StateDesc='" + StateDesc + '\'' +
                    ", LastLocTime='" + LastLocTime + '\'' +
                    ", IsInWhiteList=" + IsInWhiteList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonDevice{" +
                "RouteType=" + RouteType +
                ", Data=" + Data +
                '}';
    }
}
