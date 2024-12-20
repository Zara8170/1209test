import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import apiClient from "../../api/api";
import errorDisplay from "../../api/errorDisplay";
import { addUserInfo } from "../../redux/userSlice";

export default function Join() {
  const dispatch = useDispatch();
  const nav = useNavigate();

  const [inputValue, setInputValue] = useState({
    id: null,
    username: "",
    password: "",
    adminCode: "",
  });

  const handleChange = (e) => {
    setInputValue((prevValue) => ({
      ...prevValue,
      [e.target.name]: e.target.value,
    }));
  };

  const onSubmitAdd = async (e) => {
    e.preventDefault();
    const { username, password, adminCode } = inputValue;

    try {
      const data = {
        id: null,
        username: username,
        password: password,
        adminCode: adminCode,
      };
      const response = await apiClient.post("/api/users/join", data);
      if (response.data) {
        dispatch(addUserInfo(response.data));
        alert("가입에 성공하였습니다.");
        nav("/");
      } else {
        alert("가입에 실패하였습니다. 다시 확인해주세요.");
        return;
      }
    } catch (error) {
      errorDisplay(error);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <form
        onSubmit={onSubmitAdd}
        className="bg-white p-6 rounded-lg shadow-md w-full max-w-md"
      >
        <h2 className="text-2xl font-bold mb-5 text-center">회원가입</h2>

        <label htmlFor="username" className="block text-gray-700">
          이메일 : <span className="text-red-500">(필수!!)</span>
        </label>
        <input
          type="username"
          name="username"
          value={inputValue.username}
          onChange={handleChange}
          className="border rounded-md border-gray-300 w-full p-2 mb-4"
          placeholder="이메일을 입력하세요"
          required
        />

        <label htmlFor="password" className="block text-gray-700">
          비밀번호 : <span className="text-red-500">(필수!!)</span>
        </label>
        <input
          type="password"
          name="password"
          value={inputValue.password}
          onChange={handleChange}
          className="border rounded-md border-gray-300 w-full p-2 mb-4"
          placeholder="비밀번호를 입력하세요"
          required
        />

        <label htmlFor="adminCode" className="block text-gray-700">
          관리자 코드 (선택):
        </label>
        <input
          type="text"
          name="adminCode"
          value={inputValue.adminCode}
          onChange={handleChange}
          className="border rounded-md border-gray-300 w-full p-2 mb-4"
          placeholder="관리자 코드를 입력하세요 (선택)"
        />

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
