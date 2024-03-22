# 그래프와 트리의 차이점에 대해 설명해 주세요.

- **[그래프](https://github.com/genesis12345678/TIL/blob/main/algorithm/graph/expression/GraphExpression.md#%EA%B7%B8%EB%9E%98%ED%94%84%EC%9D%98-%ED%91%9C%ED%98%84)는 노드와 각 노드를 이어주는 간선으로 구성된 자료구조 길찾기에 이용된다.**
- **[트리](https://github.com/genesis12345678/TIL/blob/main/algorithm/tree/tree/Tree.md#%ED%8A%B8%EB%A6%AC)는 그래프의 특수한 형태로, 간선의 방향성이 있고 사이클이 되면 안 된다. 루트 노드가 아닌 노드는 모두 부모 노드를 한 개씩 갖고 있으며, 자식 노드는 최대 2개까지 가능한 구조를 갖고 있다.**

**트리는 그래프의 여러가지 종류 중 하나다. 트리는 노드와 노드 사이의 경로(간선)이 하나밖에 존재하지 않기 때문에 네트워크 방식으로 연결되어 있는 그래프보다
데이터를 찾기에 용이하다는 장점이 있다.**