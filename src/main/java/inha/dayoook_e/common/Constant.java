package inha.dayoook_e.common;

import org.springframework.stereotype.Component;

@Component
public class Constant {

    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";

    public final static String PROFILE_IMAGE_DIR = "profile";
    public final static String SONG_THUMBNAIL_DIR = "song/thumbnail";
    public final static String STORYBOOK_THUMBNAIL_DIR = "storybook/thumbnail";
    public final static String STORYBOOK_PAGE_DIR = "storybook/page";
    public final static String SONG_MEDIA_DIR = "song/media";

    public final static String CREATE_AT = "createdAt";

    //동요 완료 보상
    public final static int COMPLETE_REWARD = 20;

    public final static String SONG_COMPLETE = "_동요 완료 보상";
    public final static String STORYBOOK_COMPLETE = "_동화 완료 보상";



}