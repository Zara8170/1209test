import React, { useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useOutletContext } from "react-router-dom";
import apiClient from "../../api/api";
import errorDisplay from "../../api/errorDisplay";
import { addBook } from "../../redux/bookSlice";

export default function Add() {
  const dispatch = useDispatch();
  const nav = useNavigate();
  const { triggerCategoryRefresh } = useOutletContext();

  const titleRef = useRef(null);
  const categoryRef = useRef(null);

  const [categories, setCategories] = useState([]);
  const [inputValue, setInputValue] = useState({
    title: "",
    categoryId: "",
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await apiClient.get("/api/categories");
        setCategories(response.data);
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };
    fetchCategories();
  }, []);

  const handleChange = (e) => {
    setInputValue((prevValues) => ({
      ...prevValues,
      [e.target.name]: e.target.value,
    }));
  };

  const onSubmitAdd = async (e) => {
    e.preventDefault();
    const { title, categoryId } = inputValue;

    if (!title) {
      titleRef.current.focus();
      alert("책 제목을 입력하세요.");
      return;
    }
    if (!categoryId) {
      categoryRef.current.focus();
      alert("카테고리를 선택하세요.");
      return;
    }

    try {
      const data = {
        title: title,
        categoryId: Number(categoryId),
      };
      const response = await apiClient.post("/api/books", data);
      dispatch(addBook(response.data));
      triggerCategoryRefresh();
    } catch (error) {
      errorDisplay(error);
    }
    nav("/");
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <form
        onSubmit={onSubmitAdd}
        className="bg-white p-6 rounded-lg shadow-md w-full max-w-md"
      >
        <h2 className="text-2xl font-bold mb-5 text-center">책 추가</h2>

        <label htmlFor="title" className="block text-gray-700">
          책 제목 : <span className="text-red-500">(필수!!)</span>
        </label>
        <input
          type="text"
          name="title"
          value={inputValue.title}
          onChange={handleChange}
          ref={titleRef}
          className="border rounded-md border-gray-300 w-full p-2 mb-4"
          placeholder="책 제목을 입력하세요"
        />

        <label htmlFor="categoryId" className="block text-gray-700">
          카테고리 :
        </label>
        <select
          name="categoryId"
          value={inputValue.categoryId}
          onChange={handleChange}
          ref={categoryRef}
          className="border rounded-md border-gray-300 w-full p-2 mb-4"
        >
          <option value="">카테고리 선택</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>

        <button
          type="submit"
          className="bg-blue-500 rounded-md text-white w-full p-2 mt-4 hover:bg-blue-700"
        >
          저장
        </button>
      </form>
    </div>
  );
}
