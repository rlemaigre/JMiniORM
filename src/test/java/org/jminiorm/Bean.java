package org.jminiorm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jminiorm.attributeconverter.JsonAttributeConverter;

@Table(name = "beans", indexes = {
    @Index(name = "shortTextIndex", columnList = "short_text")
})
public class Bean {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "short_text", length = 16)
    private String shortText;
    private String longText;
    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private Integer someInt;
    private Boolean someBoolean;
    private byte[] bytes;

    @Column(name = "json")
    @Convert(converter = ListSubBeanJsonAttributeConverter.class)
    private List<SubBean> subBeans;
    @Transient
    private String notStored;

    public Bean() {
    }

    public Bean(String shortText) {
        this.shortText = shortText;
    }

    public Boolean getSomeBoolean() {
        return someBoolean;
    }

    public void setSomeBoolean(Boolean someBoolean) {
        this.someBoolean = someBoolean;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Integer getSomeInt() {
        return someInt;
    }

    public void setSomeInt(Integer someInt) {
        this.someInt = someInt;
    }

    public String getNotStored() {
        return notStored;
    }

    public void setNotStored(String notStored) {
        this.notStored = notStored;
    }

    public List<SubBean> getSubBeans() {
        return subBeans;
    }

    public void setSubBeans(List<SubBean> subBeans) {
        this.subBeans = subBeans;
    }

    public boolean compareWithoutId(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bean bean = (Bean)o;
        return Objects.equals(shortText, bean.shortText) &&
                Objects.equals(longText, bean.longText) &&
                Objects.equals(date, bean.date) &&
                Objects.equals(localDate, bean.localDate) &&
                Objects.equals(localDateTime, bean.localDateTime) &&
                Objects.equals(someInt, bean.someInt) &&
                Arrays.equals(bytes, bean.bytes) &&
                Objects.equals(subBeans, bean.subBeans) &&
                Objects.equals(notStored, bean.notStored) &&
                Objects.equals(someBoolean, someBoolean);
    }

    public static class ListSubBeanJsonAttributeConverter extends JsonAttributeConverter<List<SubBean>> {
    }
}
