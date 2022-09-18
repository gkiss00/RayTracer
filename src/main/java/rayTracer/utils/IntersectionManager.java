package rayTracer.utils;

import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntersectionManager {
    private static void removeIntersectionsInFullObjects(List<Intersection> intersections) {
        List<Intersection> tmp = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (Intersection intersection : intersections) {
            if (intersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                tmp.add(intersection);
                if (map.containsKey(intersection.getObject().getId()))
                    map.remove(intersection.getObject().getId());
                else
                    map.put(intersection.getObject().getId(), 1);
            } else {
                if (map.isEmpty())
                    tmp.add(intersection);
            }
        }
        intersections.clear();
        intersections.addAll(tmp);
    }

    private static void fusionFullObjects(List<Intersection> intersections) {
        List<Intersection> tmp = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        Intersection firstInserted = null;
        for (Intersection intersection : intersections) {
            if (intersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                if(map.isEmpty()) {
                    firstInserted = intersection;
                    tmp.add(intersection);
                }
                if (map.containsKey(intersection.getObject().getId()))
                    map.remove(intersection.getObject().getId());
                else
                    map.put(intersection.getObject().getId(), 1);
                if(map.isEmpty()) {
                    intersection.setObject(firstInserted.getObject());
                    tmp.add(intersection);
                }
            } else {
                tmp.add(intersection);
            }
        }
        intersections.clear();
        intersections.addAll(tmp);
    }

    private static void fusionBlackObjects(List<Intersection> intersections) {
        List<Intersection> tmp = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        Intersection firstInserted = null;
        for (Intersection intersection : intersections) {
            if(map.isEmpty()) {
                firstInserted = intersection;
                tmp.add(intersection);
            }
            if (map.containsKey(intersection.getObject().getId()))
                map.remove(intersection.getObject().getId());
            else
                map.put(intersection.getObject().getId(), 1);
            if(map.isEmpty()) {
                intersection.setObject(firstInserted.getObject());
                tmp.add(intersection);
            }
        }
        intersections.clear();
        intersections.addAll(tmp);
    }

    private static void createNewIntersections(List<Intersection> base, List<Intersection> black) {
        List<Intersection> all = new ArrayList<>(base.size() + black.size());
        all.addAll(base);
        all.addAll(black);
        sortIntersections(all);
        List<Intersection> res = new ArrayList<>(base.size() + black.size());
        int i = 0;
        int baseI = 0;
        boolean inBlack = false;
        boolean inBase = false;

        while(i < all.size()) {
            Intersection inter = all.get(i);
            // keep all empty for now
            if(inter.getObject().getCapacity() == CapacityTypeEnum.EMPTY) {
                //System.out.println("debug");
                ++i;
                ++baseI;
                baseI = Math.min(baseI, base.size() - 1);
                res.add(inter);
                continue;
            }

            //code here
            if(!inBase && !inBlack) {
                if(inter.getObject().getType() == ObjectTypeEnum.BASE) {
                    inBase = true;
                    res.add(inter);
                } else {
                    inBlack = true;
                }
            } else if (inBase && !inBlack) {
                if(inter.getObject().getType() == ObjectTypeEnum.BASE) {
                    inBase = false;
                    res.add(inter);
                } else {
                    inBlack = true;
                    inter.setColor(base.get(baseI).getColor());//TODO if object.noise 3D get new color at this point
                    BaseObject baseObject = (BaseObject)base.get(baseI).getObject();
                    if(baseObject.getNoiseDimension() == NoiseDimensionEnum.DIMENSION_3D) {
                        Color c = baseObject.getColorAt(inter.getPointOfIntersection());
                        inter.setColor(c);
                    }
                    inter.setObject(base.get(baseI).getObject());
                    res.add(inter);
                }
            } else if (!inBase && inBlack) {
                if(inter.getObject().getType() == ObjectTypeEnum.BASE) {
                    inBase = true;
                } else {
                    inBlack = false;
                }
            } else if (inBase && inBlack) {
                if(inter.getObject().getType() == ObjectTypeEnum.BASE) {
                    inBase = false;
                } else {
                    inBlack = false;
                    inter.setColor(base.get(baseI).getColor()); //TODO if object.noise 3D get new color at this point
                    BaseObject baseObject = (BaseObject)base.get(baseI).getObject();
                    if(baseObject.getNoiseDimension() == NoiseDimensionEnum.DIMENSION_3D) {
                        Color c = baseObject.getColorAt(inter.getPointOfIntersection());
                        inter.setColor(c);
                    }
                    inter.setObject(base.get(baseI).getObject());
                    res.add(inter);
                }
            }
            if(inter.getObject().getType() == ObjectTypeEnum.BASE && i != 0) {
                ++baseI;
                baseI = Math.min(baseI, base.size() - 1);
            }
            ++i;
        }
        base.clear();
        black.clear();
        base.addAll(res);
        all.clear();
    }

    public static void preProcessIntersections(List<Intersection> intersections, List<Intersection> blackIntersections) {
        // 1. Remove intersections of empty objects contained in full objects
        removeIntersectionsInFullObjects(intersections);
        // 2. Fusion consecutive full objects of same color
        fusionFullObjects(intersections);
        // 3. Remove and create intersections with black objects
        fusionFullObjects(blackIntersections);
        if(intersections.size() != 0 && blackIntersections.size() != 0)
            createNewIntersections(intersections, blackIntersections);
    }

    private static void sortIntersections(List<Intersection> intersections) {
        int size = intersections.size();
        for (int i = 0; i < size - 1; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; ++j) {
                if (intersections.get(j).getDistanceFromCamera() > intersections.get(j + 1).getDistanceFromCamera()) {
                    Intersection tmp = intersections.get(j);
                    intersections.set(j, intersections.get(j + 1));
                    intersections.set(j + 1, tmp);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }
    }

    public static void getIntersections(Line3D ray, List<Obj> objects, List<Intersection> intersections) {
        for (int i = 0; i < objects.size(); ++i) {
            try {
                objects.get(i).hit(ray, intersections);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        sortIntersections(intersections);
    }
}
