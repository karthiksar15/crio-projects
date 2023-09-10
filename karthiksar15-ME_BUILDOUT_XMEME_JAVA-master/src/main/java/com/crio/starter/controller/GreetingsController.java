package com.crio.starter.controller;

import com.crio.starter.exchange.MemeRequest;
import com.crio.starter.exchange.MemeResponseAll;
import com.crio.starter.exchange.MemeResponseDto;
import com.crio.starter.exchange.ResponseDto;
import com.crio.starter.model.Meme;
import com.crio.starter.service.GreetingsService;
import com.crio.starter.service.MemeService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GreetingsController {

  private final GreetingsService greetingsService;
  private final MemeService memeService;

  @GetMapping("/say-hello")
  public ResponseDto sayHello(@RequestParam String messageId) {
    return greetingsService.getMessage(messageId);
  }

  @GetMapping("/memes/")
  public ResponseEntity<List<Meme>> getMemes() {
    MemeResponseAll memeResponseDto = memeService.getMemes();
    if (memeResponseDto.getMemes() != null && memeResponseDto.getMemes().size() > 0)
      return ResponseEntity.ok().body(memeResponseDto.getMemes());
    else
      return ResponseEntity.ok().body(List.of());
  }

  @PostMapping("/memes/")
  public ResponseEntity<MemeResponseDto> saveMemes(@RequestBody MemeRequest memeRequest) {

    if(memeRequest==null)
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    if (memeRequest.getCaption()==null || memeRequest.getName()==null
        || memeRequest.getUrl()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    if (isAlreadyExists(memeRequest))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    MemeResponseDto memeResponseDto = memeService.saveMeme(memeRequest);
    if (memeResponseDto != null)
      return ResponseEntity.ok().body(memeResponseDto);
    else
      return ResponseEntity.ok().body(new MemeResponseDto());
  }

  @GetMapping("/memes/{id}")
  public ResponseEntity<MemeResponseDto> getMeme(@PathVariable String id) {
    if (id == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    MemeResponseDto memeResponseDto = memeService.findMeme(id);
    if (memeResponseDto != null)
      return ResponseEntity.ok().body(memeResponseDto);
    else
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);

  }

  public boolean isAlreadyExists(MemeRequest memeRequest) {
    MemeResponseDto memeResponseDto = memeService.findMemeWithFields(memeRequest);
    if (memeResponseDto != null)
      return true;
    return false;
  }

}
