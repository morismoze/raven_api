package com.raven.api.service.impl;

import java.sql.Timestamp;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.ServerErrorException;
import com.raven.api.model.Cover;
import com.raven.api.model.Post;
import com.raven.api.repository.CoverRepository;
import com.raven.api.service.CoverService;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.trbl.blurhash.BlurHash;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {

    private final CoverRepository coverRepository;

    private final MessageSourceAccessor accessor;
    
    @Override
    @Transactional
    public Cover createCover(final Post post, final String coverUrl) {
        final Cover cover = new Cover();
        try {
            final InputStream inputStream = new URL(coverUrl).openStream();
            final BufferedImage image = ImageIO.read(inputStream);
            System.out.println(image);
            final String blurHash = BlurHash.encode(image);

            cover.setPost(post);
            cover.setUrl(coverUrl);
            cover.setBlurHash(blurHash);
            cover.setWidth(image.getWidth());
            cover.setHeight(image.getHeight());
            cover.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            cover.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    
            return this.coverRepository.save(cover);
        } catch (IOException e) {
            throw new ServerErrorException(this.accessor.getMessage("server.error"));
        }
    }

}
