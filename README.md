### Cold Flow

소프트웨어 개발의 초기 단계에서 이루어지는 테스트

이 단계에서는 개발 환경에서 기능 테스트, 유닛 테스트, 통합 테스트 등이 이루어 짐

실제 사용 환경이 아닌 개발 환경이나 테스트 환경에서 실행

### 주요 활동:

1. **기능 테스트** 
    - 각 기능이 요구사항에 맞게 작동하는지 확인
    - Ex. 사용자가 회원가입 양식을 제출할 때, 모든 필드가 올바르게 검증되고 저장되는지 확인
2. **유닛 테스트**
    - 코드의 개별 단위(함수나 메서드)가 예상대로 작동하는지 확인
    - 예시: 특정 함수가 입력 값을 받아 올바른 결과를 반환하는지 테스트
3. **통합 테스트**
    - 여러 유닛이 함께 작동할 때 발생하는 문제를 찾음
    - 예시: 로그인 기능이 사용자 데이터베이스와 올바르게 통신하는지 확인
4. **모의 객체**
    - 실제 운영 환경 대신 모의 객체를 사용하여 테스트
    - 예시: 실제 서버 대신 모의 서버를 사용하여 API 호출을 테스트

### Cold Flow Example

1. **로그인 기능 개발 후**
    - 개발자가 직접 로그인 기능을 다양한 입력 값으로 테스트
    - 모의 서버를 사용하여 API 호출을 테스트
    - 유닛 테스트를 통해 로그인 함수가 올바른 값을 반환하는지 확인
2. **UI 구성 요소 테스트**
    - 각 버튼, 입력 필드가 올바르게 작동하는지 확인
    - 다양한 화면 크기와 해상도에서 UI가 제대로 표시되는지 확인

---

### Hot Flow

소프트웨어가 실제 운영 환경에서 테스트되는 단계

 이는 일반적으로 베타 테스트, 사용자 수락 테스트(UAT), 로드 테스트, 성능 테스트 등을 포함

### 주요 활동:

1. **베타 테스트**
    - 실제 사용자가 앱을 사용하면서 발생하는 문제를 찾아냄
    - 예시: 제한된 사용자 그룹에게 앱을 배포하여 피드백을 수집
2. **사용자 수락 테스트**
    - 최종 사용자가 실제 시나리오에서 앱을 테스트하여 요구사항이 충족되는지 확인
    - 예시: 클라이언트가 앱의 각 기능을 사용해보고, 요구사항에 맞게 동작하는지 확인
3. **성능 테스트**
    - 앱이 다양한 조건에서 얼마나 잘 작동하는지 테스트
    - 예시: 많은 사용자가 동시에 로그인할 때 서버 응답 시간이 어떻게 변하는지 확인
4. **부하 테스트**
    - 앱이 높은 부하에서도 안정적으로 작동하는지 테스트
    - 예시: 많은 사용자 요청이 동시에 들어왔을 때 시스템이 견딜 수 있는지 확인
5. **실제 데이터 사용**
    - 테스트 환경이 아닌 실제 데이터와 실제 서버를 사용하여 테스트
    - 예시: 실제 사용자 계정을 사용하여 로그인, 데이터베이스 조회, 데이터 입력 등을 테스트

### Hot Flow Example

1. **앱 출시 전 베타 테스트**
    - 제한된 사용자 그룹에게 앱을 배포
    - 사용자가 실제로 로그인하고, 데이터를 입력하고, 기능을 사용하는 과정을 모니터링
    - 사용자 피드백을 받아 버그 수정 및 기능 개선

---

## StateFlow

상태를 표현하고, 그 상태의 변화를 관찰할 수 있게 해주는 기능

이는 상태를 지속적으로 관찰하고 반응해야 하는 상황에 유용

StateFlow는 항상 현재 상태를 가지고 있으며, 관찰자들이 상태 변화를 수신할 수 있음

### 주요 특징

- 항상 초기 값을 가져야 함
- 마지막 상태를 보존하여 새로 구독하는 관찰자에게 즉시 전달
- 상태의 변화를 일관성 있게 관리할 수 있음

## StateFlow Example

```kotlin
val _stateFlow = MutableStateFlow("Initial State")
val stateFlow: StateFlow<String> get() = _stateFlow

fun updateState(newState: String) {
    _stateFlow.value = newState
}

fun observeState() {
    stateFlow.collect { state ->
        println("Current State: $state")
    }
}

fun main() = runBlocking {
    launch {
        observeState()
    }
    updateState("Updated State 1")
    updateState("Updated State 2")
}

```

`stateFlow`는 상태를 저장하고, 상태가 변할 때마다 알려준다.

---

## SharedFlow

이벤트를 표현하는 기능

상태보다는 이벤트(일회성 발생)에 적합

이벤트는 발생했을 때 구독자들에게 전달되며, 상태처럼 항상 보존되지 않음

### 주요 특징

- 초기 값이 필요 없음
- 이벤트가 발생하면 구독자에게 전달되고, 이후에는 보존되지 않음
- 다수의 구독자가 동일한 이벤트를 받을 수 있음

### SharedFlow Example

```kotlin
val _sharedFlow = MutableSharedFlow<String>()
val sharedFlow: SharedFlow<String> get() = _sharedFlow

fun emitEvent(event: String) {
    _sharedFlow.tryEmit(event)
}

fun observeEvents() {
    sharedFlow.collect { event ->
        println("Received Event: $event")
    }
}

fun main() = runBlocking {
 
    launch {
        observeEvents()
    }
    
    emitEvent("Event 1")
    emitEvent("Event 2")
}

```

`sharedFlow`는 이벤트를 발생 시키고, 발생한 이벤트를 관찰자에게 전달

---

## StateFlow vs **SharedFlow**

- **StateFlow:** 상태를 관리하고, 상태의 변화를 추적. 항상 초기 상태를 가지고 있으며, 마지막 상태를 보존
- **SharedFlow:** 이벤트를 관리하고, 일회성 이벤트를 전달. 초기 상태가 필요 없으며, 이벤트가 발생하면 구독자에게 전달되고 이후에는 보존되지 않음

---

## ViewModel Owner

`owner`는 ViewModel의 생명 주기를 관리하는 역할

Android 애플리케이션에서 ViewModel은 Activity나 Fragment와 같은 UI 컨트롤러의 데이터를 관리하고 유지하는 데 사용

여기서 "owner"는 ViewModel의 생명 주기를 소유하고 있는 컴포넌트를 의미

일반적으로는 Activity나 Fragmen 를 의미

### ViewModel과 생명 주기

ViewModel은 UI 관련 데이터를 관리하고, 해당 데이터가 UI 컨트롤러(Activity나 Fragment)의 생명 주기 동안 유지되도록 함

이는 화면 회전이나 구성 변경과 같은 상황에서도 데이터가 손실되지 않도록 보장 함

### ViewModel을 사용하는 Activity

1. **ViewModel**

```kotlin
lass MyViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data

    fun setData(newData: String) {
        _data.value = newData
    }
}

```

1. **Activity에서 ViewModel 사용:**

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myViewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myViewModel.data.observe(this, Observer { newData ->
            binding.textView.text = newData
        })

        binding.button.setOnClickListener {
            myViewModel.setData("Hello, ViewModel!")
        }
    }
}

```

### Fragment에서 ViewModel 사용

```kotlin
class ExampleFragment : Fragment(R.layout.fragment_example) {

    private var _binding: FragmentExampleBinding? = null
    private val binding get() = _binding!!

    private val myViewModel: MyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExampleBinding.bind(view)

        myViewModel.data.observe(viewLifecycleOwner, Observer { newData ->
            binding.textView.text = newData
        })

        binding.button.setOnClickListener {
            myViewModel.setData("Hello from Fragment!")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

```

### 요약

- **owner:** ViewModel의 생명 주기를 소유하는 컴포넌트(Activity 또는 Fragment)
- **LifecycleOwner:** 생명 주기 이벤트를 관찰할 수 있게 해주는 인터페이스로, ViewModel이 UI 컨트롤러의 생명 주기와 동기화 되도록 함
- **ViewModel 초기화:** Activity에서는 `by viewModels()`, Fragment에서는 `by viewModels()` 또는 `by activityViewModels()`를 사용
- **LiveData 관찰:** `observe(this, Observer { ... })`에서 `this`는 생명 주기 소유자이며, 데이터 변화를 UI에 반영

---

## Android Paging 3

대용량 데이터를 효율적으로 로드하고 표시하기 위해 사용되는 라이브러리

주로 RecyclerView와 함께 사용되며, 데이터 소스를 페이징하여 메모리 사용을 최적화하고 네트워크 및 데이터베이스 호출을 효율적으로 관리 할 수 있음

### 구성 요소

1. **PagingSource**: 데이터 소스에서 데이터를 페이징하여 로드하는 역할
2. **Pager**: PagingSource와 PagingDataAdapter를 연결하고, PagingData 객체를 생성하여 데이터 load
3. **PagingDataAdapter**: RecyclerView.Adapter를 확장한 어댑터로, PagingData 객체를 사용하여 데이터를 RecyclerView에 binding
4. **RemoteMediator**: 원격 및 로컬 데이터를 혼합하여 페이징하는 경우 사용

### 1. PagingSource 정의

PagingSource는 데이터 소스에서 데이터를 로드하는 역할

### 2. Pager 구성

Pager는 PagingSource를 사용하여 PagingData를 생성

ViewModel에서 Pager를 설정하고 Flow를 생

```kotlin
class ExampleViewModel(private val apiService: ApiService) : ViewModel() {

    val pagingDataFlow: Flow<PagingData<Data>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ExamplePagingSource(apiService) }
    ).flow
        .cachedIn(viewModelScope)
}

```

### 3. PagingDataAdapter 정의

PagingDataAdapter는 RecyclerView.Adapter를 확장하여 PagingData를 처리하는 Adapter

```kotlin
class ExampleAdapter : PagingDataAdapter<Data, ExampleAdapter.DataViewHolder>(DataComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class DataViewHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data?) {
            binding.textView.text = data?.text
        }
    }

    companion object {
        private val DataComparator = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem == newItem
        }
    }
}

```

### 4. Activity 또는 Fragment에서 RecyclerView 설정

Activity 또는 Fragment에서 RecyclerView와 어댑터를 설정하고 ViewModel 을 관리

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExampleViewModel by viewModels()
    private val adapter = ExampleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}

```

### 주요 기능 및 장점

1. **메모리 효율성**: 페이징을 통해 한 번에 로드되는 데이터 양을 제한하여 메모리 사용을 최적화
2. **자동 로드**: 사용자가 스크롤할 때 필요한 데이터를 자동으로 로드
3. **에러 처리**: 네트워크나 데이터베이스 호출 시 발생하는 오류를 쉽게 처리
4. **상태 관리**: 로딩 상태, 에러 상태 등을 관리하여 사용자에게 피드백을 제공

### 단점

1. **초기 학습 곡선**
    - Paging 3는 구성 요소가 복잡하고 개념적으로 다소 어려울 수 있음
    - 특히, 처음 사용하는 개발자에게는 이해하고 사용하는 데 시간이 걸릴 수 있음
2. **복잡한 데이터 처리**
    - 데이터 소스가 복잡하거나 다중 소스(예: 네트워크와 로컬 데이터베이스)에서 데이터를 가져오는 경우, 이를 페이징 처리하는 로직이 복잡해질 수 있음
    - `RemoteMediator`와 같은 추가 구성 요소를 사용해야 할 때 구현이 어려울 수 있음
3. **추가 보일러플레이트 코드**
    - 기본적인 데이터 로딩 외에도, 에러 처리, 로딩 상태 관리 등을 위해 추가적인 보일러플레이트 코드가 필요함
    - 이는 프로젝트의 코드베이스를 복잡하게 만들 수 있음
4. **테스트 어려움**
    - Paging 3와 관련된 코드는 테스트가 다소 복잡할 수 있음
    - 특히, `PagingSource`, `RemoteMediator` 등의 구성 요소를 단위 테스트하려면 모의 객체(Mock)를 사용해야 하는 경우가 많아 테스트 코드 작성이 어려울 수 있음
5. **성능 문제**
    - 데이터 소스의 구조나 네트워크 상태에 따라 성능 이슈가 발생할 수 있음
    - 잘못된 설정이나 최적화가 이루어지지 않은 경우, 스크롤 성능에 악영향을 미칠 수 있음
  
---

- **Navihost, Navigate, ViewModel :**  [Jetpack Compose SampleApp](https://github.com/android/compose-samples/tree/main) 의 [Jetnews](https://github.com/android/compose-samples/tree/main/JetNews) 프로젝트를 모티브로 하여 개발
    - 참고 Code
    
    ```kotlin
    // VieWModel
    sealed interface SearchUiState {
        val isLoading: Boolean
        val searchInput: String
        val errorMessages: List<LZErrorMessage?>
    
        data class NoData(
            override val isLoading: Boolean,
            override val searchInput: String,
            override val errorMessages: List<LZErrorMessage?>
        ): SearchUiState
    
        data class HasData(
            override val isLoading: Boolean,
            override val searchInput: String,
            override val errorMessages: List<LZErrorMessage?>,
            val images: List<LZDocument?>,
            val favorites: Set<LZDocument?> = emptySet(),
        ): SearchUiState
    }
    
    private data class SearchViewModelState(
        val images: List<LZDocument?> = emptyList(),
        val favorites: Set<LZDocument?> = emptySet(),
        val isLoading: Boolean = false,
        val searchInput: String = "",
        val errorMessages: List<LZErrorMessage?> = emptyList()
    ) {
    ...
    }
    
     fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it?.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
    
    // Navigation
    sealed class ScreenRoute(val screen: String) {
        object SearchScreen: ScreenRoute(LZConstants.SEARCH_SCREEN_ID) {
            fun createRoute(screenId: String) = "SEARCH/$screenId"
        }
        object BookMarkScreen : ScreenRoute(LZConstants.BOOK_MARK_SCREEN_ID) {
            fun createRoute(screenId: String) = "BOOKMARK/$screenId"
        }
    }
    ```
    
- **API 통신** : `okhttp`, `retrofit` 라이브러리 사용
- **이미지** : `coil` 라이브러리 사용
- **검색 리스트 UI** : `LazyVerticalStaggeredGrid` 사용하여 2 Cell Grid 형식으로 나타냄
- **Theme** : `MaterialTheme`사용, Blue 계열의 Dark / Light 테마 구현
