package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandData;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Used for handling Crud operations on a table + handling cache
 *
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class ForeignIslandTableManager<T extends IslandData, S> extends TableManager<T, S> {

    public ForeignIslandTableManager(ConnectionSource connectionSource, Class<T> clazz, boolean autoCommit) throws SQLException {
        super(connectionSource, clazz, autoCommit);
        sort();
    }

    @Override
    public void addEntry(T t) {
        int index = Collections.binarySearch(getEntries(), t, Comparator.comparing(t1 -> t1.getIsland().map(Island::getId).orElse(0)));
        getEntries().add(index < 0 ? -(index + 1) : index, t);
    }

    /**
     * Sort the list of entries by island id
     */
    private void sort() {
        getEntries().sort(Comparator.comparing(t1 -> t1.getIsland().map(Island::getId).orElse(0)));
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<T> getEntries(@NotNull Island island) {
        int index = getIndex(island);
        if (index == -1) return Collections.emptyList();
        int currentIndex = index - 1;
        List<T> result = new ArrayList<>();
        result.add(getEntries().get(index));

        while (true) {
            if (currentIndex < 0) break;
            IslandData t = getEntries().get(currentIndex);
            if (island.equals(t.getIsland().orElse(null))) {
                result.add(getEntries().get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index + 1;

        while (true) {
            if (currentIndex >= getEntries().size()) break;
            IslandData t = getEntries().get(currentIndex);
            if (island.equals(t.getIsland().orElse(null))) {
                result.add(getEntries().get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Gets the index of the island -1 if not found
     *
     * @param island The specified island
     * @return The index where its located
     */
    private int getIndex(@NotNull Island island) {
        int first = 0;
        int last = getEntries().size() - 1;
        int mid = last / 2;
        while (first <= last) {
            IslandData islandData = getEntries().get(mid);
            int islandId = islandData.getIsland().isPresent() ? islandData.getIsland().get().getId() : 0;
            if (islandId < island.getId()) {
                first = mid + 1;
            } else if (islandId == island.getId()) {
                return mid;
            } else {
                last = mid - 1;
            }
            mid = (first + last) / 2;
        }
        return -1;
    }
}
