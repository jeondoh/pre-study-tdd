package com.course.prestudy;

import com.course.prestudy.domain.board.entity.Board;
import com.course.prestudy.domain.board.repository.BoardRepository;
import com.course.prestudy.domain.user.entity.User;
import com.course.prestudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                User user = User.makeDummyUser("user" + i, "pwd" + i);
                userRepository.save(user);
            });
        }

        List<User> users = userRepository.findAll();
        IntStream.rangeClosed(1, 105).forEach(i -> {
            int userIndex = (i - 1) % users.size();
            User user = users.get(userIndex);

            Board board = Board.makeDummyBoard(
                    user,
                    "제목" + i,
                    i + "번째 게시글 내용",
                    "pwd" + i
            );

            boardRepository.save(board);
        });
    }
}
