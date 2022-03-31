package com.example.demo.service;

import com.example.demo.advice.exception.NotOwnerException;
import com.example.demo.advice.exception.ResourceNotExistsException;
import com.example.demo.advice.exception.UserNotFoundException;
import com.example.demo.domain.Board;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.ParamsPost;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Board findBoard(String boardName) {
        return Optional.ofNullable(boardRepository.findByName(boardName)).orElseThrow(ResourceNotExistsException::new);
    }

    public List<Post> findPosts(String boardName) {
        return postRepository.findByBoard(findBoard(boardName));
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(ResourceNotExistsException::new);
    }

    /**
     * 게시물을 등록합니다. 게시물의 회원ID가 조회되지 않으면 UserNotFoundException 처리합니다.
     * @param userId
     * @param boardName
     * @param params
     * @return
     * @throws UserNotFoundException
     */
    public Post writePost(long userId, String boardName, ParamsPost params) {
        Board board = findBoard(boardName);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = new Post(user, board, params.getAuthor(), params.getTitle(), params.getContent());
        return postRepository.save(post);
    }

    /**
     * 게시물을 수정합니다. 게시물 등록자와 로그인 회원정보가 틀리면 NotOwnerException 처리합니다.
     * @param postId
     * @param userId
     * @param params
     */
    public Post updatePost(long postId, String userId, ParamsPost params) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!userId.equals(user.getId())) {
            throw new NotOwnerException();
        }
        post.setUpdate(params.getAuthor(), params.getTitle(), params.getContent());
        return post;
    }

    /**
     * 게시물을 삭제합니다. 게사물 등록자와 로그인 회원정보가 틀리면 NotOwnerException 처리합니다.
     * @param postId
     * @param userId
     * @return
     */
    public boolean deletePost(long postId, String userId) {
        Post post = getPost(postId);
        User user = post.getUser();
        if (!userId.equals(user.getId())) {
            throw new NotOwnerException();
        }
        postRepository.delete(post);
        return true;
    }


}
