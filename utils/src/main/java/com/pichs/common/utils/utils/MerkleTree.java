package com.pichs.common.utils.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zrs
 * @param <H> hash type
 */
public class MerkleTree<H extends Comparable<H>> {

    List<H> hashArray;
    List<H> treeArray;
    int treeHigh;
    int bottomOffset;

    /**
     *
     * @param <D>
     * @param dataNodes
     * @param dataHashFunction
     * @param treeHashFunction
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public <D> MerkleTree(List<D> dataNodes, Function<D, H> dataHashFunction, BiFunction<H, H, H> treeHashFunction) {
//        System.out.println("dataNodes = " + dataNodes);
        hashArray = dataNodes.stream().map(dataHashFunction).collect(Collectors.toList());
//        System.out.println("hashArray = " + hashArray);
        treeHigh = getTreeHigh(hashArray.size());
//        System.out.println("treeHigh = " + treeHigh);
        bottomOffset = treeHigh == 1 ? 0 : (1 << treeHigh - 1) - 1;
//        System.out.println("bottomOffset = " + bottomOffset);
        int treeSize;
        // tree size
        if (treeHigh == 1) {
            treeSize = 1;
        } else {
            int bottomLayerSize, othersSize;
            // 1 -> 0, 2 -> 1, 3 -> 3, 4 -> 7, 5 -> 15, 6 -> 31
            othersSize = (1 << (treeHigh - 1)) - 1;
            // 3-> 2, 4-> 2, 5 -> 3, 6->3, 7 -> 4, 8->4, 9->5
            bottomLayerSize = (hashArray.size() + 1) / 2;
            treeSize = bottomLayerSize + othersSize;
        }
//        System.out.println("treeSize = " + treeSize);
        treeArray = new ArrayList<>(treeSize);
        for (int i = 0; i < treeSize; i++) {
            treeArray.add(null);
        }
        for (int i = treeSize - 1; i >= 0; i--) {
//            System.out.println(" -- i = " + i);
            boolean bottomLayer = i >= bottomOffset;
//            System.out.println("bottomLayer = " + bottomLayer);
            if (bottomLayer) {
                // bottom layer. read data hash directly
                int bottomIndex = i - bottomOffset;
                int dataIndexLeft = bottomIndex * 2;
                int dataIndexRight = bottomIndex * 2 + 1;
                H hashLeft = getDataHash(dataIndexLeft);
//                System.out.println("hashLeft = " + hashLeft);
                H hashRight = getDataHash(dataIndexRight);
//                System.out.println("hashRight = " + hashRight);
                H treeHash = treeHashFunction.apply(hashLeft, hashRight);
//                System.out.println("treeHash = " + treeHash);
                treeArray.set(i, treeHash);
            } else {
                // tree layer, read tree children hash
                int leftChildIndex = getLeftChildIndex(i);
                int rightChildIndex = getRightChildIndex(i);

                H hashLeft = getTreeHash(leftChildIndex);
                H hashRight = getTreeHash(rightChildIndex);
                H treeHash = treeHashFunction.apply(hashLeft, hashRight);
//                System.out.println("treeHash = " + treeHash);
                treeArray.set(i, treeHash);
            }

        }
//        System.out.println("treeArray.size() = " + treeArray.size());
//        for (H h : treeArray) {
//            System.out.println("    - " + h);
//        }
    }

    public final int getTreeHigh(int dataSize) {
//        System.out.println("dataSize = " + dataSize);
        if (dataSize == 1) {
            return 1;
        }

        int leadingZero = Integer.numberOfLeadingZeros(dataSize - 1);
//        System.out.println("leadingZero = " + leadingZero);
        return 32 - leadingZero;
    }

    public final H getDataHash(int index) {
        return getOrNull(hashArray, index);
    }

    public final H getTreeHash(int index) {
        return getOrNull(treeArray, index);
    }

    public final <E> E getOrNull(List<E> array, int index) {
        if (index < 0 || index >= array.size()) {
            return null;
        }
        return array.get(index);
    }

    public final int getBottomTreeIndex(int dataIndex) {
        int offset = 1 << (treeHigh - 1);
        return dataIndex / 2 + offset;
    }

    public final int getTreeSize(int dataSize) {
        // 1 -> 1, 2 -> 1, 3 -> 2, 4 -> 2, 5 -> 3, 8 -> 3, 9 -> 4, 16 -> 4
        int layers = Integer.highestOneBit(dataSize + 1) - 1;
        // tree size

        if (layers == 1) {
            return 1;
        } else {
            int bottomLayerSize, othersSize;
            othersSize = (1 << layers - 1) - 1;
            // 3-> 2, 4-> 2, 5 -> 3, 6->3, 7 -> 4, 8->4, 9->5
            bottomLayerSize = (dataSize + 1) / 2;
            return bottomLayerSize + othersSize;
        }

    }

    public final int getLeftChildIndex(int index) {
        // 0 -> 1,  1-> 3, 2->5, 3->7, 4->9, 5 -> 11, 
        if (index < 0 || index > treeArray.size()) {
            return -1;
        }
        int left = index * 2 + 1;
        if (left >= treeArray.size()) {
            return -1;
        }
        return left;
    }

    public final int getRightChildIndex(int index) {
        // 0 -> 2,  1-> 4, 2->6, 3->8, 4->10, 5 -> 12, 
        if (index < 0 || index > treeArray.size()) {
            return -1;
        }
        int right = (index + 1) * 2;
        if (right >= treeArray.size()) {
            return -1;
        }
        return right;
    }

    public final int getParentIndex(int index) {
        // 0 -> -1,  1-> 0, 2->0, 3->1, 4->1, 5 -> 2, 6 -> 2, 
        if (index < 0 || index > treeArray.size()) {
            return -1;
        }
        int parent = (index - 1) / 2;
        if (parent < 0) {
            return -1;
        }
        return parent;
    }

    /**
     *
     * @param another
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Integer> hashValidate(MerkleTree<H> another) {
        return hashValidate(another.treeArray, another.hashArray);
    }

    /**
     *
     * @param checkingTreeArray
     * @param checkingHashArray
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Integer> hashValidate(List<H> checkingTreeArray, List<H> checkingHashArray) {
        if (hashArray.size() != checkingHashArray.size()) {
            throw new IllegalStateException("can only do comparison for same blocks,"
                    + " expecting " + hashArray.size()
                    + " but got " + checkingHashArray.size()
            );
        }
        if (this.treeArray.size() != checkingTreeArray.size()) {
            throw new IllegalStateException("can only do comparison for same tree,"
                    + " expecting " + treeArray.size()
                    + " but got " + checkingTreeArray.size()
            );
        }

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(0);
        // 广度遍历
        int checkingLayer = 1;
        while (true) {

//            System.out.println("layer = " + checkingLayer + " pending check indexes = " + queue);
            if (checkingLayer != treeHigh) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    int checkingIndex = queue.poll();
                    H selfHash = treeArray.get(checkingIndex);
                    H checkingHash = checkingTreeArray.get(checkingIndex);
//                    System.out.println("--checkingIndex = " + checkingIndex);
//                    System.out.println("  selfHash = " + selfHash);
//                    System.out.println("  checkingHash = " + checkingHash);
                    if (selfHash.compareTo(checkingHash) == 0) {
                        // equals
                    } else {
                        int leftChildIndex = getLeftChildIndex(checkingIndex);
                        int rightChildIndex = getRightChildIndex(checkingIndex);
                        if (leftChildIndex != -1) {
                            queue.add(leftChildIndex);
                        }
                        if (rightChildIndex != -1) {
                            queue.add(rightChildIndex);
                        }
                    }
                }
//                System.out.println("after check queue = " + queue);
                if (checkingLayer == 1 && queue.isEmpty()) {
                    // first layer and match. just think is exact same
                    return Collections.EMPTY_LIST;
                }
            } else {
                // last layer , check and return
//                System.out.println("last layer = " + checkingLayer + " bottom offset " + bottomOffset);
                List<Integer> results = new ArrayList<>();

                for (int noMatchIndex : queue) {
                    int bottomIndex = noMatchIndex - bottomOffset;
                    int dataIndexLeft = bottomIndex * 2;
                    int dataIndexRight = bottomIndex * 2 + 1;
                    Stream.of(dataIndexLeft, dataIndexRight).filter(dataIndex -> {
                        H dataHash = getOrNull(hashArray, dataIndex);
                        H checkingHash = getOrNull(checkingHashArray, dataIndex);

//                        System.out.println("--final data hash check index = " + dataIndex);
//                        System.out.println("  dataHash = " + dataHash);
//                        System.out.println("  checkingHash = " + checkingHash);
                        return dataHash == null ? checkingHash == null : dataHash.compareTo(checkingHash) != 0;
                    }).forEach(results::add);
                }
                return results;
            }

            checkingLayer++;
        }
    }

//    public static void main(String[] args) {

//        MerkleTree<Integer> simplestTree = new MerkleTree<>(List.of(1, 2, 3, 4, 5, 6), i -> i, (i1, i2) -> {
//            if (i1 == null && i2 == null) {
//                throw new IllegalStateException();
//            } else if (i1 == null) {
//                return i2;
//            } else if (i2 == null) {
//                return i1;
//            } else {
//                return i1 + i2;
//            }
//        });
//        
//        
//        List<String> testString = IntStream.range(0, 40).mapToObj(i -> Integer.toString(i)).collect(Collectors.toList());
//        List<String> testString2 = new ArrayList<>(testString);
//        List<Integer> expectingResult = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            int randModifyIndex = (int) (Math.random() * testString2.size());
//
//            testString2.set(randModifyIndex, "modified " + testString2.get(i));
////            System.out.println("modified  = " + randModifyIndex);
//            expectingResult.add(randModifyIndex);
//        }
//        MerkleTree<String> merkleTree1 = new MerkleTree<>(testString, s -> s, (s1, s2) -> s1 + "," + s2);
//        MerkleTree<String> merkleTree2 = new MerkleTree<>(testString2, s -> s, (s1, s2) -> s1 + "," + s2);
//        List<Integer> differentBlocks = merkleTree1.hashValidate(merkleTree2);
//        System.out.println("differentBlocks = " + differentBlocks);
//    }
}
