package com.example.sowitplot.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010J8\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00152\b\u0010\u0018\u001a\u0004\u0018\u00010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0019"}, d2 = {"Lcom/example/sowitplot/ui/PlotViewModel;", "Landroidx/lifecycle/ViewModel;", "dao", "Lcom/example/sowitplot/data/PlotDao;", "(Lcom/example/sowitplot/data/PlotDao;)V", "plots", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/sowitplot/data/PlotEntity;", "getPlots", "()Landroidx/lifecycle/LiveData;", "deletePlot", "", "plot", "renamePlot", "newName", "", "savePlot", "name", "encoded", "centerLat", "", "centerLng", "areaSqMeters", "thumbnailPath", "app_debug"})
public final class PlotViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.sowitplot.data.PlotDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.sowitplot.data.PlotEntity>> plots = null;
    
    public PlotViewModel(@org.jetbrains.annotations.NotNull()
    com.example.sowitplot.data.PlotDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.sowitplot.data.PlotEntity>> getPlots() {
        return null;
    }
    
    public final void savePlot(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String encoded, double centerLat, double centerLng, double areaSqMeters, @org.jetbrains.annotations.Nullable()
    java.lang.String thumbnailPath) {
    }
    
    public final void deletePlot(@org.jetbrains.annotations.NotNull()
    com.example.sowitplot.data.PlotEntity plot) {
    }
    
    public final void renamePlot(@org.jetbrains.annotations.NotNull()
    com.example.sowitplot.data.PlotEntity plot, @org.jetbrains.annotations.NotNull()
    java.lang.String newName) {
    }
}