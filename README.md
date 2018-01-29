# HCCP-Patronus

要怎么才能把事情做得像想象中的那样美好呢？

### 组件使用方法

#### HightlightingCode类

函数接口：

```java
public VirtualizedScrollPane HLCodeArea (double prf_width,double prf_height, String code)
//宽，高为初始的大小，code表示传入需要显示的代码。
```

调用实例：

```java
@FXML private AnchorPane edit_page;
......
HighlightingCode highlighter = new HighlightingCode ();
VirtualizedScrollPane vz = highlighter.HLCodeArea (edit_page.getPrefWidth(),edit_page.getHeight ());

edit_page.getChildren ().add (vz);

//这个方法在edit_page里面，生成一个指定宽高的文本编辑器，且不传入Code参数时，会使用默认的代码。
```

