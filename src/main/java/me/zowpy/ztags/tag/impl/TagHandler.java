package me.zowpy.ztags.tag.impl;

import lombok.Getter;
import me.zowpy.ztags.Tags;
import me.zowpy.ztags.handler.Handler;
import me.zowpy.ztags.handler.Loader;
import me.zowpy.ztags.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagHandler implements Handler {

    private TagLoader tagLoader;
    @Getter private static List<Tag> tags = new ArrayList<>();

    public TagHandler() {
        tagLoader = new TagLoader();
    }

    @Override
    public Loader getLoader() {
        return tagLoader;
    }


}
