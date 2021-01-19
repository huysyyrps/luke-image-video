package com.example.luke_imagevideo_send.cehouyi.bean;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dataName : f001
         * dataItem : 01
         * itemData : [{"A":"1","B":"1","C":"1"}]
         */

        private String dataName;
        private String dataItem;
        private List<ItemDataBean> itemData;

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public String getDataItem() {
            return dataItem;
        }

        public void setDataItem(String dataItem) {
            this.dataItem = dataItem;
        }

        public List<ItemDataBean> getItemData() {
            return itemData;
        }

        public void setItemData(List<ItemDataBean> itemData) {
            this.itemData = itemData;
        }

        public static class ItemDataBean {
            /**
             * A : 1
             * B : 1
             * C : 1
             */

            private String A;
            private String B;
            private String C;

            public String getA() {
                return A;
            }

            public void setA(String A) {
                this.A = A;
            }

            public String getB() {
                return B;
            }

            public void setB(String B) {
                this.B = B;
            }

            public String getC() {
                return C;
            }

            public void setC(String C) {
                this.C = C;
            }
        }
    }
}
