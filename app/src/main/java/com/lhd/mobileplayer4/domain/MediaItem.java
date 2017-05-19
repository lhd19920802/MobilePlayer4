package com.lhd.mobileplayer4.domain;

import java.io.Serializable;

/**
 * Created by lihuaidong on 2017/5/18 20:53.
 * 微信：lhd520ssp
 * QQ:414320737
 * 作用：视频或音频信息基类
 */
public class MediaItem implements Serializable
{
    private String name;
    private long duration;
    private long size;
    private String data;//视频播放地址
    private String artist;

    public MediaItem()
    {
    }

    public MediaItem(String name, long duration, long size, String data, String artist)
    {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.data = data;
        this.artist = artist;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    @Override
    public String toString()
    {
        return "MediaItem{" +
               "name='" + name + '\'' +
               ", duration=" + duration +
               ", size=" + size +
               ", data='" + data + '\'' +
               ", artist='" + artist + '\'' +
               '}';
    }
}
