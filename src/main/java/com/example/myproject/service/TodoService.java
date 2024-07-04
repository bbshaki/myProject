package com.example.myproject.service;

import com.example.myproject.dto.TodoDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Todo;
import com.example.myproject.repository.AttractionRepository;
import com.example.myproject.repository.FestivalRepository;
import com.example.myproject.repository.MemberUserRepository;
import com.example.myproject.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    private final MemberUserRepository memberUserRepository;
    private final AttractionRepository attractionRepository;
    private final FestivalRepository festivalRepository;

    public Long addTodo(TodoDTO todoDTO, Principal principal){
        MemberUser memberUser = memberUserRepository.findMemberUserById(principal.getName());
        Attraction attraction = new Attraction();
        attraction.setAno(todoDTO.getAno());

        Festival festival = new Festival();
        festival.setFno(todoDTO.getFno());

        Todo todo = modelMapper.map(todoDTO, Todo.class);

        if (todoDTO.getAno() != null){
            todo.setAttraction(attraction);
            todo.setMemberUser(memberUser);
        }
        if (todoDTO.getFno() != null){
            todo.setFestival(festival);
            todo.setMemberUser(memberUser);
        }
        return todoRepository.save(todo).getTno();
    }

    public List<TodoDTO> todoDTOList(Principal principal){
        MemberUser memberUser = memberUserRepository.findMemberUserById(principal.getName());
        List<Todo> todoList = todoRepository.findByMemberUser(memberUser);
        List<TodoDTO> todoDTOList = new ArrayList<>();
        for (Todo todo : todoList){
            TodoDTO todoDTO = new TodoDTO();
            todoDTO.setTno(todo.getTno());
            todoDTO.setMno(todo.getMemberUser().getMno());
            if (todo.getFestival() != null){
                todoDTO.setFno(todo.getFestival().getFno());
                todoDTO.setTitle(todo.getFestival().getTitle());
            } else if (todo.getAttraction() != null){
                todoDTO.setAno(todo.getAttraction().getAno());
                todoDTO.setTitle(todo.getAttraction().getTitle());
            }
            todoDTOList.add(todoDTO);
        }
        todoDTOList.forEach(todoDTO -> log.info("값이 머라고 나오나~~~~~" + todoDTOList));
        return todoDTOList;
    }

    public void deleteTodo(Long tno){
        log.info("그러면 여기는 수행하니????????????????????????");
        todoRepository.deleteById(tno);
    }

}
