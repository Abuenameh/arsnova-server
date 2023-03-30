package net.particify.arsnova.comments.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.particify.arsnova.comments.model.Comment;

@Service
public class CommentFindQueryService {

  private static final Logger logger = LoggerFactory.getLogger(CommentFindQueryService.class);

  private final CommentService commentService;

  @Autowired
  public CommentFindQueryService(final CommentService commentService) {
    this.commentService = commentService;
  }

  public Set<UUID> resolveQuery(final FindQuery<Comment> findQuery) {
    Set<UUID> commentIds = new HashSet<>();
    if (findQuery.getProperties().getRoomId() != null) {
      List<Comment> contentList = commentService.getByRoomIdAndArchiveIdNull(findQuery.getProperties().getRoomId());
      for (Comment c : contentList) {
        if (findQuery.getProperties().isAck() == c.isAck()) {
          commentIds.add(c.getId());
        }
      }
    }

    return commentIds;
  }
}
