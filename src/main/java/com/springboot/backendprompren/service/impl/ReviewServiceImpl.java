package com.springboot.backendprompren.service.impl;

import com.springboot.backendprompren.config.security.JwtTokenProvider;
import com.springboot.backendprompren.data.dto.response.ResponseReviewDto;
import com.springboot.backendprompren.data.dto.response.ResponseReviewListDto;
import com.springboot.backendprompren.data.dto.resquest.RequestReviewDto;
import com.springboot.backendprompren.data.entity.Prompt;
import com.springboot.backendprompren.data.entity.Review;
import com.springboot.backendprompren.data.entity.User;
import com.springboot.backendprompren.data.repository.PromptRepository;
import com.springboot.backendprompren.data.repository.ReviewRepository;
import com.springboot.backendprompren.data.repository.UserRepository;
import com.springboot.backendprompren.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final PromptRepository promptRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             PromptRepository promptRepository,
                             UserRepository userRepository,
                             JwtTokenProvider jwtTokenProvider) {
        this.reviewRepository = reviewRepository;
        this.promptRepository = promptRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseReviewDto saveReview(RequestReviewDto requestReviewDto, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);

        User user = userRepository.getByAccount(account);
        Prompt prompt = promptRepository.findById(requestReviewDto.getPromptId())
                .orElseThrow(() -> new IllegalArgumentException("프롬프트를 찾을 수 없습니다."));


        Review review = new Review();
        review.setUser(user);
        review.setPrompt(prompt);
        review.setTitle(requestReviewDto.getTitle());
        review.setContent(requestReviewDto.getContent());
        review.setStar(requestReviewDto.getStar());
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        LOGGER.info("[savePrompt] saved ReviewId : {}", savedReview.getReview_id());
        LOGGER.info("[savePrompt] saved UId : {}", savedReview.getUser().getUid());
        LOGGER.info("[savePrompt] saved Prompt_id : {}", savedReview.getPrompt().getPrompt_id());

        ResponseReviewDto responseReviewDto = new ResponseReviewDto();
        responseReviewDto.setReview_id(savedReview.getReview_id());
        responseReviewDto.setTitle(savedReview.getTitle());
        responseReviewDto.setContent(savedReview.getContent());
        responseReviewDto.setStar(savedReview.getStar());
        responseReviewDto.setReview_writer(review.getUser().getNickname());
        responseReviewDto.setPrompt_title(review.getPrompt().getTitle());
        responseReviewDto.setWriter_thumbnail(review.getUser().getThumbnail());
        responseReviewDto.setPrompt_image(review.getPrompt().getImage());
        responseReviewDto.setCreatedAt(String.valueOf(savedReview.getCreatedAt()));



        return responseReviewDto;
    }

    @Override
    public ResponseReviewListDto getReviewList(Long prompt_id, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        ModelMapper mapper = new ModelMapper();
        List<ResponseReviewDto> responseReviewDtoList = new ArrayList<>();
        ResponseReviewListDto responseReviewListDto = new ResponseReviewListDto();

        String token = jwtTokenProvider.resolveToken(servletRequest);

        if(jwtTokenProvider.validationToken(token)) {
            String account = jwtTokenProvider.getUsername(token);
            User user = userRepository.getByAccount(account);
            Prompt prompt = promptRepository.getById(prompt_id);
            LOGGER.info("[getToDoList] review 조회를 진행합니다. account : {}", account);
            List<Review> reviewList = reviewRepository.findAllByUserAndPrompt(user,prompt);
            for(Review review : reviewList){
                ResponseReviewDto responseReviewDto = mapper.map(review, ResponseReviewDto.class);
                responseReviewDto.setReview_writer(review.getUser().getNickname());
                responseReviewDto.setPrompt_title(review.getPrompt().getTitle());
                responseReviewDto.setWriter_thumbnail(review.getUser().getThumbnail());
                responseReviewDto.setPrompt_image(review.getPrompt().getImage());
                responseReviewDtoList.add(responseReviewDto);
            }
            responseReviewListDto.setItems(responseReviewDtoList);
            LOGGER.info("[getToDoList] review 조회가 완료되었습니다. account : {}", account);
        }
        return responseReviewListDto;
    }

    @Override
    public ResponseReviewListDto getTop4ReviewList(Long prompt_id,
                                                   HttpServletRequest servletRequest,
                                                   HttpServletResponse servletResponse) {
        ModelMapper mapper = new ModelMapper();
        List<ResponseReviewDto> responseReviewDtoList = new ArrayList<>();
        ResponseReviewListDto responseReviewListDto = new ResponseReviewListDto();

        String token = jwtTokenProvider.resolveToken(servletRequest);

        if (jwtTokenProvider.validationToken(token)) {
            String account = jwtTokenProvider.getUsername(token);
            User user = userRepository.getByAccount(account);
            Prompt prompt = promptRepository.getById(prompt_id);
            LOGGER.info("[getReviewList] review 조회를 진행합니다. account : {}", account);

            // 최근 4개의 리뷰를 가져옵니다.
            List<Review> reviewList = reviewRepository.findTop4ByUserAndPromptOrderByCreatedAtDesc(user,prompt);
            for (Review review : reviewList) {
                ResponseReviewDto responseReviewDto = mapper.map(review, ResponseReviewDto.class);
                responseReviewDto.setReview_writer(review.getUser().getNickname());
                responseReviewDto.setPrompt_title(review.getPrompt().getTitle());
                responseReviewDto.setWriter_thumbnail(review.getUser().getThumbnail());
                responseReviewDto.setPrompt_image(review.getPrompt().getImage());
                responseReviewDtoList.add(responseReviewDto);
            }
            responseReviewListDto.setItems(responseReviewDtoList);
            LOGGER.info("[getReviewList] review 조회가 완료되었습니다. account : {}", account);
        }
        return responseReviewListDto;
    }


    @Override
    public long countReviewForPrompt(Long prompt_id, HttpServletRequest servletRequest,
                                     HttpServletResponse servletResponse) {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getUsername(token);
        User user = userRepository.getByAccount(account);
        Prompt prompt = promptRepository.findById(prompt_id)
                .orElseThrow(() -> new IllegalArgumentException("프롬프트를 찾을 수 없습니다."));
        return reviewRepository.countByUserAndPrompt(user,prompt);
    }

}
