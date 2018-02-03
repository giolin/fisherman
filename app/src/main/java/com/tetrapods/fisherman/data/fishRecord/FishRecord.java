package com.tetrapods.fisherman.data.fishRecord;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Ann on 03/02/2018.
 */
@Entity
public class FishRecord {

  @Id(autoincrement = true)
  private Long id;

  private String date;
  private String startTime;
  private String longitude;
  private String latitude;
  private String catches;
  private String weightUnit;
  private String totalTime;
  private String timeUnit;
  @Generated(hash = 1191078930)
  public FishRecord(Long id, String date, String startTime, String longitude,
          String latitude, String catches, String weightUnit, String totalTime,
          String timeUnit) {
      this.id = id;
      this.date = date;
      this.startTime = startTime;
      this.longitude = longitude;
      this.latitude = latitude;
      this.catches = catches;
      this.weightUnit = weightUnit;
      this.totalTime = totalTime;
      this.timeUnit = timeUnit;
  }
  @Generated(hash = 1047849438)
  public FishRecord() {
  }
  public Long getId() {
      return this.id;
  }
  public void setId(Long id) {
      this.id = id;
  }
  public String getDate() {
      return this.date;
  }
  public void setDate(String date) {
      this.date = date;
  }
  public String getStartTime() {
      return this.startTime;
  }
  public void setStartTime(String startTime) {
      this.startTime = startTime;
  }
  public String getLongitude() {
      return this.longitude;
  }
  public void setLongitude(String longitude) {
      this.longitude = longitude;
  }
  public String getLatitude() {
      return this.latitude;
  }
  public void setLatitude(String latitude) {
      this.latitude = latitude;
  }
  public String getCatches() {
      return this.catches;
  }
  public void setCatches(String catches) {
      this.catches = catches;
  }
  public String getWeightUnit() {
      return this.weightUnit;
  }
  public void setWeightUnit(String weightUnit) {
      this.weightUnit = weightUnit;
  }
  public String getTotalTime() {
      return this.totalTime;
  }
  public void setTotalTime(String totalTime) {
      this.totalTime = totalTime;
  }
  public String getTimeUnit() {
      return this.timeUnit;
  }
  public void setTimeUnit(String timeUnit) {
      this.timeUnit = timeUnit;
  }
}
