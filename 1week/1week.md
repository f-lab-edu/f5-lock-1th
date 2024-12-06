# 
# 목표

- CMS GC와 G1GC의 성능을 비교
- CMS GC를 왜 deprecated시켰는지 이해하기
- G1GC가 해결한 문제


# 환경구성

테스트를 위해서 kotlin application서버와 ngrinder, pinpoint를 활용한다. 우선, ngrinder에 대한 설명과 사용방법은 아래 글을 참조해서 세팅했다.
(https://github.com/binghe819/TIL/blob/master/Infra&DevOps/nGrinder/nGrinder%20%EC%99%84%EB%B2%BD%EA%B0%80%EC%9D%B4%EB%93%9C.md )
pinpoint는 apm도구인데 datadog와 grafana+prometheus를 사용해본적은 있지만 pinpoint는 처음 사용해본다. 일단 datadog와 비교했을때 pinpoint가 보여주는 지표들은 조금 부족해 보이지만 비용을 들이지 않고 무료로 간편하게 사용하기에는 쓸만해 보인다.



# 1차 테스트

CMS GC와 G1GC를 비교하기 위해서는 일단 GC가 발생할 수 있도록 트래픽을 유입시켜야 한다.


테스트에서 지표로 확인하려는 내용은 아래와 같고 지표별 근거를 작성했다.

1. full gc발생시 애플리케이션 중단시간 : full gc는 stw를 일으킨다. 따라서 stw시간이 짧으면서  heap에서 많은 메모리를 회수하는것은 gc선정에 중요한 지표이다.
2. full gc빈도 : full gc는 stw와 항상 함께한다. 따라서 너무 많은 full gc는 애플리케이션의 성능에 악영향을 주므로 중요한 지표이다
3. full gc이후 메모리 감소율 : 짧은 시간동안 얼마나 많은 힙 메모리를 회수하는지 확인하기 위함이다. 시간만 짧고 회수하는 메모리가 너무 적으면 비효율적이지 않을까 싶다
4. full gc발생시 cpu 사용량 : 객체를 스캔하고 힙메모리 내부에서 이동하고 삭제하는 과정으로 cpu 사용률이 올라간다. 같은 환경에서 어느정도의 full gc발생시 cpu률은 gc선정에 중요한 지표이다.




결과를 먼저 적으면 1차 테스트는 의미없는 테스트였다. 확인하려는 내용을 제대로 확인하지 못했다. GC확인을 위해 api요청에 의도적으로 메모리를 점유하는 코드를 작성했는데 테스트를 돌리고 나서 생각해보니 이렇게 하면 GC에 의해 수거하는게 아니라 OLD 영역에 상주하면서 jvm heap memory를 모두 사용해서 결국 oom이 발생한다

1차 테스트는 실패했지만 성능테스트에서 중요한 것은 테스트를 통해 얻을 지표 및 결과를 잘 정의해야하고 결과달성을 위한 전제조건을 잘 세팅하는게 중요하다. 나는 여기서 전제조건에 해당하는 full gc발생환경을 잘못세팅했으므로 의미없는 부하테스트를 진행했다.



## 1.1 G1GC
OOM으로 pinpoint agent가 죽어버려서 지표 확인 실패

## 1.2 CMS GC
이것 역시 의미 없는 차트이다..

![image](https://github.com/user-attachments/assets/92380bd2-4777-4d7d-a412-4f9cc9d16f59)







# 2. 2차 테스트

1차테스트를 실패했으므로 2차테스트를 진행한다.



## 2.1 G1GC


## 2.2 CMS GC






# 3. CMS GC의 문제점




# 4. G1GC가 해결한 문제
